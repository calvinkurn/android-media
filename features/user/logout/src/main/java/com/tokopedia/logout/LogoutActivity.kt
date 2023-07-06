package com.tokopedia.logout

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
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
import com.gojek.kyc.plus.OneKycConstants
import com.gojek.kyc.plus.getKycSdkDocumentDirectoryPath
import com.gojek.kyc.plus.getKycSdkFrameDirectoryPath
import com.gojek.kyc.plus.getKycSdkLogDirectoryPath
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.firebase.TkpdFirebaseAnalytics
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.core.gcm.NotificationModHandler
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.logout.di.DaggerLogoutComponent
import com.tokopedia.logout.di.LogoutComponent
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.notifications.CMPushNotificationManager.Companion.instance
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.sessioncommon.data.Token.Companion.getGoogleClientId
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.util.EncoderDecoder
import com.tokopedia.utils.file.FileUtil
import java.io.File
import javax.inject.Inject

/**
 * @author rival
 * @created 29-01-2020
 *
 * @applink : [com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.LOGOUT]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME]
 * default is 'true', set 'false' if you wan get activity result
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_IS_CLEAR_DATA_ONLY]
 * default is 'false', set 'true' if you just wan to clear data only
 */

class LogoutActivity : BaseSimpleActivity(), HasComponent<LogoutComponent> {

    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var userSessionDataStore: UserSessionDataStore

    @Inject
    lateinit var dataStorePreference: DataStorePreference

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val logoutViewModel by lazy { viewModelProvider.get(LogoutViewModel::class.java) }

    private var isReturnToHome = true
    private var isClearDataOnly = false
    private var isSaveSession = false

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): LogoutComponent {
        return DaggerLogoutComponent.builder()
            .baseComponent((application as BaseMainApplication).baseAppComponent)
            .context(this)
            .build()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        component.inject(this)
        userSession = UserSession(this)

        getParam()

        initObservable()
        initGoogleClient()

        showLoading()
        saveLoginReminderData()

        if (isClearDataOnly) {
            clearData()
        } else {
            if(isSaveSession) {
                logoutViewModel.doLogout(LogoutUseCase.PARAM_SAVE_SESSION)
            } else {
                logoutViewModel.doLogout()
            }
        }
    }

    private fun getParam() {
        if (intent.extras != null) {
            isReturnToHome = intent.extras?.getBoolean(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, true) as Boolean
            isClearDataOnly = intent.extras?.getBoolean(ApplinkConstInternalUserPlatform.PARAM_IS_CLEAR_DATA_ONLY, false) as Boolean
            isSaveSession = intent.extras?.getBoolean(ApplinkConstInternalUserPlatform.PARAM_IS_SAVE_SESSION, false) as Boolean
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

    private fun initObservable() {
        logoutViewModel.logoutResult.observe(
            this,
            Observer {
                when (it) {
                    is Success -> {
                        clearData()
                    }
                    is Fail -> {
                        hideLoading()
                        processingError(it)
                    }
                }
            }
        )
    }

    private fun processingError(it: Fail) {
        val errorMessage = it.throwable.message.toString()
        if (isByPassClearData(errorMessage)) {
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
        clearCacheGotoKyc()
        disconnectTokoChat()
        clearStickyLogin()
        logoutGoogleAccountIfExist()
        TrackApp.getInstance().moEngage.logoutEvent()
        PersistentCacheManager.instance.delete()
        sendBroadcastToAppWidget()

        // need to implement delete use case
        NotificationModHandler.clearCacheAllNotification(applicationContext)

        dismissAllActivedNotifications()
        clearWebView()
        clearLocalChooseAddress()
        clearRegisterPushNotificationPref()
        clearTemporaryTokenForSeamless()
        instance.refreshFCMTokenFromForeground(userSession.deviceId, true)

        userSession.logoutSession()
        TkpdFirebaseAnalytics.getInstance(this).setUserId(null)

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

    fun dismissAllActivedNotifications() {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun sendBroadcastToAppWidget() {
        if (GlobalConfig.isSellerApp()) {
            val i = Intent()
            i.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            applicationContext.sendBroadcast(i)
        }
    }

    private fun logoutGoogleAccountIfExist() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (googleSignInAccount != null) mGoogleSignInClient.signOut()
    }

    private fun clearStickyLogin() {
        val stickyPref = applicationContext.getSharedPreferences(STICKY_LOGIN_PREF, Context.MODE_PRIVATE)
        stickyPref.edit().clear().apply()
    }

    private fun clearLocalChooseAddress() {
        val chooseAddressPref = applicationContext.getSharedPreferences(CHOOSE_ADDRESS_PREF, Context.MODE_PRIVATE)
        chooseAddressPref.edit().clear().apply()
    }

    private fun saveLoginReminderData() {
        try {
            val encryptedUsername = EncoderDecoder.Encrypt(userSession.name, UserSession.KEY_IV)
            val encryptedProfilePicture = EncoderDecoder.Encrypt(userSession.profilePicture, UserSession.KEY_IV)

            getSharedPreferences(STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)?.edit()?.apply {
                putString(KEY_USER_NAME, encryptedUsername).apply()
                putString(KEY_PROFILE_PICTURE, encryptedProfilePicture).apply()
            }
        } catch (e: Exception) {
            // skip save login reminder data
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun clearRegisterPushNotificationPref() {
        getSharedPreferences(REGISTER_PUSH_NOTIFICATION_PREFERENCE, Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
    }

    private fun showLoading() {
        findViewById<View>(R.id.logoutLoading)?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        findViewById<View>(R.id.logoutLoading)?.visibility = View.GONE
    }

    private fun clearTemporaryTokenForSeamless() {
        val sharedPrefs = getSharedPreferences(
            GOTO_SEAMLESS_PREF,
            Context.MODE_PRIVATE
        )
        sharedPrefs.edit().clear().apply()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun clearWebView() {
        try {
            WebView(applicationContext).clearCache(true)
            val cookieManager: CookieManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.createInstance(this)
                cookieManager = CookieManager.getInstance()
                cookieManager.removeAllCookie()
            } else {
                cookieManager = CookieManager.getInstance()
                cookieManager.removeAllCookies {}
            }
        } catch (ignored: Exception) {}
    }

    private fun disconnectTokoChat() {
        if (application is AbstractionRouter) {
            (application as AbstractionRouter).disconnectTokoChat()
        }
    }

    private fun clearCacheGotoKyc() {
        try {
            val preferenceName = OneKycConstants.KYC_SDK_PREFERENCE_NAME
            val preferenceKey = OneKycConstants.KYC_UPLOAD_PROGRESS_STATE
            val preference = applicationContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

            val state = preference.getString(preferenceKey, "").orEmpty()
            if (state.isNotEmpty()) {
                preference.edit().remove(preferenceKey).apply()
            }

            val directory1 = getKycSdkDocumentDirectoryPath(this)
            val directory2 = getKycSdkFrameDirectoryPath(this)
            val directory3 = getKycSdkLogDirectoryPath(this)
            removeGotoKycImage(directory1)
            removeGotoKycImage(directory2)
            removeGotoKycImage(directory3)
        } catch (ignored: Exception) {}
    }

    private fun removeGotoKycImage(directory: String) {
        val file = File(directory)
        if (file.isDirectory) {
            FileUtil.deleteFolder(directory)
        }
    }

    companion object {
        private const val STICKY_LOGIN_PREF = "sticky_login_widget.pref"
        private const val STICKY_LOGIN_REMINDER_PREF = "sticky_login_reminder.pref"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_PROFILE_PICTURE = "profile_picture"
        private const val CHOOSE_ADDRESS_PREF = "local_choose_address"
        private const val INVALID_TOKEN = "Token tidak valid."

        const val GOTO_SEAMLESS_PREF = "goto_seamless_pref"
        const val KEY_TEMPORARY = "temporary_key"

        /**
         * class [com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker]
         */
        private const val REGISTER_PUSH_NOTIFICATION_PREFERENCE = "registerPushNotification"
    }
}
