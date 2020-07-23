package com.tokopedia.logout.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.core.gcm.FCMCacheManager
import com.tokopedia.core.gcm.NotificationModHandler
import com.tokopedia.core.util.AppWidgetUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.logout.R
import com.tokopedia.logout.di.DaggerLogoutComponent
import com.tokopedia.logout.di.LogoutComponent
import com.tokopedia.logout.viewmodel.LogoutViewModel
import com.tokopedia.notifications.CMPushNotificationManager.Companion.instance
import com.tokopedia.sessioncommon.data.Token.Companion.GOOGLE_API_KEY
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_logout.*
import javax.inject.Inject

/**
 * @author rival
 * @created 29-01-2020
 *
 * @applink : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.LOGOUT]
 * @param   : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME]
 * default is 'true', set 'false' if you wan get activity result
 */

class LogoutActivity : BaseSimpleActivity(), HasComponent<LogoutComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val logoutViewModel by lazy { viewModelProvider.get(LogoutViewModel::class.java) }

    private var isReturnToHome = true

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private var tetraDebugger: TetraDebugger? = null

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): LogoutComponent {
        return DaggerLogoutComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent
        ).build()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        component.inject(this)

        getParams()

        initTetraDebugger()
        initObservable()
        initGoogleClient()

        showLoading()
        logoutViewModel.doLogout()
    }

    private fun getParams() {
        if (intent.extras != null) {
            isReturnToHome = intent.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME, true) as Boolean
        }
    }

    private fun initGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).apply {
            requestIdToken(GOOGLE_API_KEY)
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
        logoutViewModel.logoutLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    clearData()
                }
                is Fail -> {
                    hideLoading()
                    DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                        setTitle(getString(R.string.logout))
                        setDescription(it.throwable.message.toString())
                        setPrimaryCTAText(getString(R.string.try_again))
                        setCancelable(false)
                        setOverlayClose(false)
                        setPrimaryCTAClickListener {
                            clearData()
                            dismiss()
                        }
                    }.show()
                }
            }
        })
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
        CacheApiClearAllUseCase(applicationContext).executeSync()

        val notify = NotificationModHandler(applicationContext)
        notify.dismissAllActivedNotifications()

        instance.refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(applicationContext), true)

        tetraDebugger?.setUserId("")

        if (isReturnToHome) {
            if (GlobalConfig.isSellerApp()) {
                val mIntent = RouteManager.getIntent(this, ApplinkConst.LOGIN).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(mIntent)
                finish()
            } else {
                RouteManager.route(this, ApplinkConst.HOME)
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
        LoginManager.getInstance().logOut()
    }

    private fun clearStickyLogin() {
        val stickyPref = applicationContext.getSharedPreferences(STICKY_LOGIN_PREF, Context.MODE_PRIVATE)
        stickyPref.edit().clear().apply()
    }

    private fun showLoading() {
        logoutLoading?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        logoutLoading?.visibility = View.GONE
    }

    companion object {
        private const val STICKY_LOGIN_PREF = "sticky_login_widget.pref"
    }
}