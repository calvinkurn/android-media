package com.tokopedia.managepassword.forgotpassword.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.managepassword.ManagePasswordWebViewActivity
import com.tokopedia.managepassword.common.ManagePasswordConstant.KEY_IS_CONTAINS_LOGIN_APPLINK
import com.tokopedia.managepassword.common.ManagePasswordConstant.PARAM_AUTO_FILL
import com.tokopedia.managepassword.di.DaggerManagePasswordComponent
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.managepassword.di.module.ManagePasswordModule
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.KEY_URL
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

    private var uri: Uri? = null

    override fun getScreenName(): String {
        return SCREEN_FORGOT_PASSWORD
    }

    override fun getComponent(): ManagePasswordComponent {
        return DaggerManagePasswordComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .managePasswordModule(ManagePasswordModule(this))
                .build()
    }

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        gotoWebView(urlResetPassword())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_WEB_VIEW -> {
                    if (data != null && data.extras != null) {
                        val isContainsLoginApplink = data.extras?.getBoolean(KEY_IS_CONTAINS_LOGIN_APPLINK) ?: false
                        val url = data.extras?.getString(KEY_URL) ?: ""
                        uri = Uri.parse(url).buildUpon().build()

                        if (isContainsLoginApplink) {
                            uri?.let {
                                if (userSession.isLoggedIn && isForceLogout(it)) {
                                    gotoLogout()
                                } else {
                                    gotoLogin(it)
                                }
                            }
                        }
                    }
                }
                REQUEST_CODE_LOGOUT -> gotoLogin()
                REQUEST_CODE_LOGIN -> gotoHome()
            }
        } else {
            gotoHome()
        }
    }

    private fun gotoWebView(url: String) {
        val intent = ManagePasswordWebViewActivity.createIntent(this, url)
        startActivityForResult(intent, REQUEST_CODE_WEB_VIEW)
    }

    private fun isForceLogout(uri: Uri): Boolean =
        uri.path?.contains(CLEAR_CACHE_PREFIX) == true

    private fun gotoLogout() {
        val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.LOGOUT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_CLEAR_DATA_ONLY, true)
        startActivityForResult(intent, REQUEST_CODE_LOGOUT)
    }

    private fun gotoLogin(uri: Uri? = null) {
        val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
        if(uri != null) {
            val email = uri.getQueryParameter(QUERY_PARAM_EMAIL)
            val phone = uri.getQueryParameter(QUERY_PARAM_PHONE)

            if (!email.isNullOrEmpty()) {
                intent.putExtra(PARAM_AUTO_FILL, decodeParam(email))
                userSession.autofillUserData = decodeParam(email)
            } else if (!phone.isNullOrEmpty()) {
                intent.putExtra(PARAM_AUTO_FILL, decodeParam(phone))
                userSession.autofillUserData = decodeParam(phone)
            }
        }
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun gotoHome() {
        val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun decodeParam(value: String): String {
        val byteArray = Base64.decode(value, Base64.DEFAULT)
        return String(byteArray)
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
        private const val REQUEST_CODE_LOGOUT = 101
        private const val REQUEST_CODE_LOGIN = 102

        const val QUERY_PARAM_EMAIL = "e"
        const val QUERY_PARAM_PHONE = "p"

        private const val CLEAR_CACHE_PREFIX = "clear-cache"

        private const val SCREEN_FORGOT_PASSWORD = "Forgot password page"
        private const val URL_FORGOT_PASSWORD = "https://m.tokopedia.com/reset-password"
        private const val REMOTE_FORGOT_PASSWORD_DIRECT_TO_WEBVIEW_URL = "android_forgot_password_webview_url"
    }
}