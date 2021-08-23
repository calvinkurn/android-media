package com.tokopedia.logout.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analyticsdebugger.debugger.TetraDebugger
import com.tokopedia.analyticsdebugger.debugger.TetraDebugger.Companion.instance
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.core.gcm.FCMCacheManager
import com.tokopedia.core.gcm.NotificationModHandler
import com.tokopedia.core.util.AppWidgetUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.logout.R
import com.tokopedia.logout.di.DaggerLogoutComponent
import com.tokopedia.logout.di.LogoutComponent
import com.tokopedia.logout.di.module.LogoutModule
import com.tokopedia.logout.viewmodel.LogoutViewModel
import com.tokopedia.notifications.CMPushNotificationManager.Companion.instance
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.sessioncommon.data.Token.Companion.getGoogleClientId
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_logout.*
import javax.inject.Inject

/**
 * @author rival
 * @created 29-01-2020
 *
 * @applink : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.LOGOUT]
 * @param   : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME]
 * default is 'true', set 'false' if you wan get activity result
 * @param   : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_CLEAR_DATA_ONLY]
 * default is 'false', set 'true' if you just wan to clear data only
 */

class LogoutActivity : BaseSimpleActivity(), HasComponent<LogoutComponent> {

    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val logoutViewModel by lazy { viewModelProvider.get(LogoutViewModel::class.java) }

    private var isReturnToHome = true
    private var isClearDataOnly = false

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var tetraDebugger: TetraDebugger? = null

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): LogoutComponent {
        return DaggerLogoutComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .logoutModule(LogoutModule(this))
                .build()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        component.inject(this)
        userSession = UserSession(this)

        getParams()

        initTetraDebugger()
        initObservable()
        initGoogleClient()

        showLoading()
        saveLoginReminderData()

        if (isClearDataOnly) {
            clearData()
        } else {
            logoutViewModel.doLogout()
        }
    }

    private fun getParams() {
        if (intent.extras != null) {
            isReturnToHome = intent.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME, true) as Boolean
            isClearDataOnly = intent.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_CLEAR_DATA_ONLY, false) as Boolean
        }
    }

    private fun initGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).apply {
            requestIdToken(getGoogleClientId(this@LogoutActivity))
            requestEmail()
            requestProfile()
        }.build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun initTetraDebugger() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            tetraDebugger = instance(applicationContext)
            tetraDebugger?.init()
        }
    }

    private fun initObservable() {
        logoutViewModel.logoutResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    clearData()
                }
                is Fail -> {
                    hideLoading()
                    processingError(it)
                }
            }
        })
    }

    private fun processingError(it: Fail) {
        val errorMessage = it.throwable.message.toString()
        if(isByPassClearData(errorMessage)) {
            clearData()
        } else {
            showErrorDialog(errorMessage)
        }

    }

    private fun isByPassClearData(errorMessage: String): Boolean {
        return errorMessage == INVALID_TOKEN
    }

    private fun showErrorDialog(errorMessage: String) {
        DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.logout))
            setDescription(errorMessage)
            setPrimaryCTAText(getString(R.string.try_again))
            setCancelable(false)
            setOverlayClose(false)
            setPrimaryCTAClickListener {
                clearData()
                dismiss()
            }
        }.show()
    }

    private fun clearData() {
        hideLoading()
        clearStickyLogin()
        logoutFacebook()
        logoutGoogleAccountIfExist()
        TrackApp.getInstance().moEngage.logoutEvent()
        PersistentCacheManager.instance.delete()
        AppWidgetUtil.sendBroadcastToAppWidget(applicationContext)
        NotificationModHandler.clearCacheAllNotification(applicationContext)
        NotificationModHandler(applicationContext).dismissAllActivedNotifications()
        clearWebView()
        clearLocalChooseAddress()
        clearOccData()

        instance.refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(applicationContext), true)

        tetraDebugger?.setUserId("")
        userSession.clearToken()
        userSession.logoutSession()
        RemoteConfigInstance.getInstance().abTestPlatform.fetchByType(null)

        if (isReturnToHome) {
            if (GlobalConfig.isSellerApp()) {
                val mIntent = RouteManager.getIntent(this, ApplinkConst.LOGIN).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(mIntent)
                finish()
            } else {
                val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        } else {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun logoutGoogleAccountIfExist() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (googleSignInAccount != null) mGoogleSignInClient.signOut()
    }

    private fun logoutFacebook() {
        FacebookSdk.sdkInitialize(applicationContext)
        LoginManager.getInstance().logOut()
    }

    private fun clearStickyLogin() {
        val stickyPref =  applicationContext.getSharedPreferences(STICKY_LOGIN_PREF, Context.MODE_PRIVATE)
        stickyPref.edit().clear().apply()
    }

    private fun clearLocalChooseAddress() {
        val chooseAddressPref = applicationContext.getSharedPreferences(CHOOSE_ADDRESS_PREF, Context.MODE_PRIVATE)
        chooseAddressPref.edit().clear().apply()
    }

    private fun saveLoginReminderData() {
        getSharedPreferences(STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)?.edit()?.apply {
            putString(KEY_USER_NAME, userSession.name).apply()
            putString(KEY_PROFILE_PICTURE, userSession.profilePicture).apply()
        }
    }

    private fun showLoading() {
        logoutLoading?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        logoutLoading?.visibility = View.GONE
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun clearWebView() {
        try {
            WebView(applicationContext).clearCache(true)
            val cookieManager: CookieManager
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.createInstance(this)
                cookieManager = CookieManager.getInstance()
                cookieManager.removeAllCookie()
            } else {
                cookieManager = CookieManager.getInstance()
                cookieManager.removeAllCookies {}
            }
        } catch (ignored: Exception) {}
    }

    private fun clearOccData() {
        val occDataPref = applicationContext.getSharedPreferences(OCC_DATA_PREF, Context.MODE_PRIVATE)
        occDataPref.edit().clear().apply()
    }

    companion object {
        private const val STICKY_LOGIN_PREF = "sticky_login_widget.pref"
        private const val STICKY_LOGIN_REMINDER_PREF = "sticky_login_reminder.pref"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_PROFILE_PICTURE = "profile_picture"
        private const val CHOOSE_ADDRESS_PREF = "local_choose_address"
        private const val OCC_DATA_PREF = "occ_remove_profile_ticker"
        private const val INVALID_TOKEN = "Token tidak valid."
    }
}