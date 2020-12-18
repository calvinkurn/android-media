package com.tokopedia.loginregister.login.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract

/**
 * @author by nisie on 10/1/18.
 */
open class LoginActivity : BaseSimpleActivity(), HasComponent<LoginRegisterComponent> {

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        if (toolbar != null) toolbar.setPadding(0, 0, 30, 0)
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putAll(getBundleFromData())
        intent?.extras?.let {
            bundle.putAll(it)
        }

        return LoginEmailPhoneFragment.createInstance(bundle)
    }

    override fun getComponent(): LoginRegisterComponent {
        return DaggerLoginRegisterComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        setWhiteStatusBarIfSellerApp()
        removeBackButtonIfSellerApp()
    }

    private fun setWhiteStatusBarIfSellerApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && GlobalConfig.isSellerApp()) {
            setStatusBarColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            setLightStatusBar(true)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.parent_view) is LoginEmailPhoneContract.View) {
            (supportFragmentManager.findFragmentById(R.id
                    .parent_view) as LoginEmailPhoneContract.View).onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    private fun removeBackButtonIfSellerApp() {
        if (GlobalConfig.isSellerApp()) {
            toolbar?.setPadding(30, 0, 0, 0)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun getBundleFromData(): Bundle {
        val bundle = Bundle()
        intent?.data?.let {
            val method = it.getQueryParameter(PARAM_LOGIN_METHOD).orEmpty()
            val phone = it.getQueryParameter(PARAM_PHONE).orEmpty()
            val email = it.getQueryParameter(PARAM_EMAIL).orEmpty()
            val password = it.getQueryParameter(PARAM_PASSWORD).orEmpty()
            val source = it.getQueryParameter(PARAM_SOURCE).orEmpty()

            bundle.putString(PARAM_LOGIN_METHOD, method)
            bundle.putString(PARAM_PHONE, phone)
            bundle.putString(PARAM_EMAIL, email)
            bundle.putString(PARAM_PASSWORD, password)
            bundle.putString(PARAM_SOURCE, source)
        }

        return bundle
    }

    companion object {
        const val PARAM_LOGIN_METHOD = "method"
        const val PARAM_PHONE = "phone"
        const val PARAM_EMAIL = "email"
        const val PARAM_PASSWORD = "password"
        const val PARAM_SOURCE = "source"
    }
}
