package com.tokopedia.loginregister.login.view.activity

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.METHOD_LOGIN_PHONE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract
import com.tokopedia.telemetry.ITelemetryActivity

/**
 * @author by nisie on 10/1/18.
 */
open class LoginActivity : BaseSimpleActivity(), HasComponent<LoginComponent>,
    ITelemetryActivity {

    private val loginComponent: LoginComponent by lazy {
        ActivityComponentFactory.instance.createLoginComponent(application)
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putAll(getBundleFromData())
        intent?.extras?.let {
            bundle.putAll(it)
        }
        return LoginEmailPhoneFragment.createInstance(bundle)
    }

    override fun getComponent(): LoginComponent {
        return loginComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWhiteStatusBarIfSellerApp()
        removeBackButtonIfSellerApp()
    }

    private fun setWhiteStatusBarIfSellerApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && GlobalConfig.isSellerApp()) {
            setStatusBarColor(
                MethodChecker.getColor(
                    this,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
            setLightStatusBar(true)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.parent_view) is LoginEmailPhoneContract.View) {
            (supportFragmentManager.findFragmentById(R.id.parent_view) as LoginEmailPhoneContract.View).onBackPressed()
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
            var method = it.getQueryParameter(PARAM_LOGIN_METHOD).orEmpty()
            val phone = it.getQueryParameter(PARAM_PHONE).orEmpty()
            val email = it.getQueryParameter(PARAM_EMAIL).orEmpty()
            val source = it.getQueryParameter(PARAM_SOURCE).orEmpty()
            val isReturnHomeWhenBackPressed = it.getBooleanQueryParameter(PARAM_IS_RETURN_HOME, false)

            if (method.isEmpty()) {
                if (email.isNotEmpty()) {
                    method = METHOD_LOGIN_EMAIL
                } else if (phone.isNotEmpty()) {
                    method = METHOD_LOGIN_PHONE
                }
            }

            bundle.putString(PARAM_LOGIN_METHOD, method)
            bundle.putString(PARAM_PHONE, phone)
            bundle.putString(PARAM_EMAIL, email)
            bundle.putString(PARAM_SOURCE, source)
            bundle.putBoolean(PARAM_IS_RETURN_HOME, isReturnHomeWhenBackPressed)
        }

        return bundle
    }

    override fun getToolbarResourceID(): Int {
        return R.id.unifytoolbar
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_login_register
    }

    companion object {
        const val PARAM_LOGIN_METHOD = "method"
        const val PARAM_PHONE = "p"
        const val PARAM_EMAIL = "e"
        const val PARAM_SOURCE = "source"
    }

    override fun getTelemetrySectionName() = "login"
}
