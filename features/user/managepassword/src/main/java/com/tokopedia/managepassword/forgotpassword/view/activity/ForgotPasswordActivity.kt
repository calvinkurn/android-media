package com.tokopedia.managepassword.forgotpassword.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.managepassword.ManagePasswordWebViewActivity
import com.tokopedia.managepassword.common.ManagePasswordConstant.KEY_IS_CONTAINS_LOGIN_APPLINK
import com.tokopedia.managepassword.di.DaggerManagePasswordComponent
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.managepassword.di.module.ManagePasswordModule
import com.tokopedia.managepassword.forgotpassword.view.fragment.ForgotPasswordFragment
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author rival
 * @created 14/05/2020
 * @team : @minion-kevin
 *
 * For navigating to this class
 * @applink : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.FORGOT_PASSWORD]
 * @params
 * required : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL]
 * optional : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_AUTO_RESET]
 * optional : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_REMOVE_FOOTER]
 */

class ForgotPasswordActivity : BaseSimpleActivity(), HasComponent<ManagePasswordComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private val isDirectToWebView: Boolean
        get() = getAbTestPlatform()?.getString(AB_TEST_RESET_PASSWORD_KEY) == AB_TEST_RESET_PASSWORD

    override fun getScreenName(): String {
        return SCREEN_FORGOT_PASSWORD
    }

    override fun getComponent(): ManagePasswordComponent {
        return DaggerManagePasswordComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .managePasswordModule(ManagePasswordModule(this))
                .build()
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent?.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ForgotPasswordFragment.createInstance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        getAbTestPlatform()?.fetch(null)

        if (isDirectToWebView) {
            gotoWebView(urlResetPassword())
            return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null && data.extras != null ) {
            when(requestCode) {
                REQUEST_CODE_WEB_VIEW -> {
                    val isContainsLoginApplink = data.extras?.getBoolean(KEY_IS_CONTAINS_LOGIN_APPLINK) ?: false
                    if (isContainsLoginApplink) {
                        gotoLogin()
                    }
                }
                REQUEST_CODE_LOGIN -> {
                    if (userSession.isLoggedIn) gotoHome()
                }
            }
        } else {
            finish()
        }
    }

    private fun gotoWebView(url: String) {
        if (userSession.isLoggedIn) {
            val intent = ManagePasswordWebViewActivity.createIntent(this, url, false)
            startActivityForResult(intent, REQUEST_CODE_WEB_VIEW)
        } else {
            val intent = ManagePasswordWebViewActivity.createIntent(this, url)
            startActivityForResult(intent, REQUEST_CODE_WEB_VIEW)
        }
    }

    private fun gotoLogin() {
        val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun gotoHome() {
        val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
        startActivity(intent)
        finish()
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(this.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun urlResetPassword(): String {
        val url = RemoteConfigInstance.getInstance().abTestPlatform.getString(REMOTE_FORGOT_PASSWORD_DIRECT_TO_WEBVIEW_URL, "")
        return if (url.isEmpty()) {
            URL_FORGOT_PASSWORD
        } else {
            url
        }
    }

    companion object {
        private const val REQUEST_CODE_WEB_VIEW = 100
        private const val REQUEST_CODE_LOGIN = 101

        private const val SCREEN_FORGOT_PASSWORD = "Forgot password page"
        private const val URL_FORGOT_PASSWORD = "https://m.tokopedia.com/reset-password"
        private const val REMOTE_FORGOT_PASSWORD_DIRECT_TO_WEBVIEW_URL = "android_forgot_password_webview_url"
        private const val AB_TEST_RESET_PASSWORD_KEY = "Reset Password AND"
        private const val AB_TEST_RESET_PASSWORD = "Reset Password AND"
    }
}