package com.tokopedia.logintest.login.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.logintest.R
import com.tokopedia.logintest.common.di.DaggerLoginRegisterTestAppComponent
import com.tokopedia.logintest.common.di.LoginRegisterTestAppComponent
import com.tokopedia.logintest.login.view.fragment.LoginTestAppFragment
import com.tokopedia.logintest.login.view.listener.LoginTestAppContract

/**
 * @author by nisie on 10/1/18.
 */
class LoginTestAppActivity : BaseSimpleActivity(), HasComponent<LoginRegisterTestAppComponent> {

    companion object {

        val METHOD_WEBVIEW = 333
        val METHOD_EMAIL = 444

        val AUTO_WEBVIEW_NAME = "webview_name"
        val AUTO_WEBVIEW_URL = "webview_url"
    }

    object DeepLinkIntents {

        @JvmStatic
        fun getAutoLoginWebview(context: Context, name: String, url: String): Intent {
            val intent = Intent(context, LoginTestAppActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(LoginTestAppFragment.IS_AUTO_LOGIN, true)
            bundle.putInt(LoginTestAppFragment.AUTO_LOGIN_METHOD, METHOD_WEBVIEW)
            bundle.putString(AUTO_WEBVIEW_NAME, name)
            bundle.putString(AUTO_WEBVIEW_URL, url)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun getIntentLoginFromRegister(context: Context, email: String): Intent {
            val intent = Intent(context, LoginTestAppActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(LoginTestAppFragment.IS_FROM_REGISTER, true)
            bundle.putBoolean(LoginTestAppFragment.IS_AUTO_FILL, true)
            bundle.putString(LoginTestAppFragment.AUTO_FILL_EMAIL, email)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun getAutomaticLogin(context: Context, email: String, password: String, source : String): Intent {
            val intent = Intent(context, LoginTestAppActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(LoginTestAppFragment.IS_AUTO_LOGIN, true)
            bundle.putBoolean(LoginTestAppFragment.IS_FROM_REGISTER, true)
            bundle.putInt(LoginTestAppFragment.AUTO_LOGIN_METHOD, METHOD_EMAIL)
            bundle.putString(LoginTestAppFragment.AUTO_LOGIN_EMAIL, email)
            bundle.putString(LoginTestAppFragment.AUTO_LOGIN_PASS, password)
            bundle.putString(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
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

        return LoginTestAppFragment.createInstance(bundle)
    }

    override fun getComponent(): LoginRegisterTestAppComponent {
        return DaggerLoginRegisterTestAppComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.parent_view) is LoginTestAppContract.View) {
            (supportFragmentManager.findFragmentById(R.id
                    .parent_view) as LoginTestAppContract.View).onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setWhiteStatusBarIfSellerApp()
        removeBackButtonIfSellerApp()
    }

    private fun setWhiteStatusBarIfSellerApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && GlobalConfig.isSellerApp()) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    private fun removeBackButtonIfSellerApp() {
        if (GlobalConfig.isSellerApp()) {
            toolbar?.setPadding(30, 0, 0, 0)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }
}
