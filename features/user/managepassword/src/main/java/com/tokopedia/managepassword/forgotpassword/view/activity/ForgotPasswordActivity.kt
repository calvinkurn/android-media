package com.tokopedia.managepassword.forgotpassword.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.managepassword.di.DaggerManagePasswordComponent
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.managepassword.di.module.ManagePasswordModule
import com.tokopedia.managepassword.forgotpassword.view.fragment.ForgotPasswordFragment
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

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
            RouteManager.route(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, urlResetPassword()))
            finish()
            return
        }
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
        private const val SCREEN_FORGOT_PASSWORD = "Forgot password page"
        private const val URL_FORGOT_PASSWORD = "https://m.tokopedia.com/reset-password"
        private const val REMOTE_FORGOT_PASSWORD_DIRECT_TO_WEBVIEW_URL = "android_forgot_password_webview_url"
        private const val AB_TEST_RESET_PASSWORD_KEY = "Reset Password AND"
        private const val AB_TEST_RESET_PASSWORD = "Reset Password AND"
    }
}