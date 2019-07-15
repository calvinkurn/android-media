package com.tokopedia.loginregister.login.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment

import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.user.session.UserSession

/**
 * @author by nisie on 10/1/18.
 */
class LoginActivity : BaseSimpleActivity(), HasComponent<LoginRegisterComponent> {

    companion object {

        val METHOD_FACEBOOK = 111
        val METHOD_GOOGLE = 222
        val METHOD_WEBVIEW = 333
        val METHOD_EMAIL = 444

        val AUTO_WEBVIEW_NAME = "webview_name"
        val AUTO_WEBVIEW_URL = "webview_url"
    }

    object DeepLinkIntents {

        @JvmStatic
        @DeepLink(ApplinkConst.LOGIN)
        fun getCallingApplinkIntent(context: Context, bundle: Bundle): Intent {
            val uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon()
            val userSession = UserSession(context)
            if (userSession.isLoggedIn) {
                return RouteManager.getIntent(context, ApplinkConst.HOME)
            } else {
                val intent = getCallingIntent(context)
                return intent.setData(uri.build())
            }
        }

        @JvmStatic
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }

        @JvmStatic
        fun getAutoLoginGoogle(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(LoginEmailPhoneFragment.IS_AUTO_LOGIN, true)
            bundle.putInt(LoginEmailPhoneFragment.AUTO_LOGIN_METHOD, METHOD_GOOGLE)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun getAutoLoginFacebook(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(LoginEmailPhoneFragment.IS_AUTO_LOGIN, true)
            bundle.putInt(LoginEmailPhoneFragment.AUTO_LOGIN_METHOD, METHOD_FACEBOOK)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun getAutoLoginWebview(context: Context, name: String, url: String): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(LoginEmailPhoneFragment.IS_AUTO_LOGIN, true)
            bundle.putInt(LoginEmailPhoneFragment.AUTO_LOGIN_METHOD, METHOD_WEBVIEW)
            bundle.putString(AUTO_WEBVIEW_NAME, name)
            bundle.putString(AUTO_WEBVIEW_URL, url)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun getIntentLoginFromRegister(context: Context, email: String): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(LoginEmailPhoneFragment.IS_FROM_REGISTER, true)
            bundle.putBoolean(LoginEmailPhoneFragment.IS_AUTO_FILL, true)
            bundle.putString(LoginEmailPhoneFragment.AUTO_FILL_EMAIL, email)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun getAutomaticLogin(context: Context, email: String, password: String): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(LoginEmailPhoneFragment.IS_AUTO_LOGIN, true)
            bundle.putBoolean(LoginEmailPhoneFragment.IS_FROM_REGISTER, true)
            bundle.putInt(LoginEmailPhoneFragment.AUTO_LOGIN_METHOD, METHOD_EMAIL)
            bundle.putString(LoginEmailPhoneFragment.AUTO_LOGIN_EMAIL, email)
            bundle.putString(LoginEmailPhoneFragment.AUTO_LOGIN_PASS, password)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        if (toolbar != null) toolbar.setPadding(0, 0, 30, 0)
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        return LoginEmailPhoneFragment.createInstance(bundle)
    }

    override fun getComponent(): LoginRegisterComponent {
        return DaggerLoginRegisterComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.parent_view) is LoginEmailPhoneContract.View) {
            (supportFragmentManager.findFragmentById(R.id
                    .parent_view) as LoginEmailPhoneContract.View).onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

}
