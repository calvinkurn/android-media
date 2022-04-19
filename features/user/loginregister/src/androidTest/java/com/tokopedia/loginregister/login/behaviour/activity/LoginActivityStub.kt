package com.tokopedia.loginregister.login.behaviour.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.login.behaviour.di.LoginComponentStub
import com.tokopedia.loginregister.login.behaviour.fragment.LoginEmailPhoneFragmentStub
import com.tokopedia.loginregister.login.view.activity.LoginActivity

class LoginActivityStub: LoginActivity() {

    lateinit var loginComponentStub: LoginComponentStub

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    override fun initializeLoginComponent(): LoginComponentStub {
        return loginComponentStub
    }

    private fun getBundleFromData(): Bundle {
        val bundle = Bundle()
        intent?.data?.let {
            var method = it.getQueryParameter(PARAM_LOGIN_METHOD).orEmpty()
            val phone = it.getQueryParameter(PARAM_PHONE).orEmpty()
            val email = it.getQueryParameter(PARAM_EMAIL).orEmpty()
            val source = it.getQueryParameter(PARAM_SOURCE).orEmpty()

            if (method.isEmpty()) {
                if (email.isNotEmpty()) {
                    method = ApplinkConstInternalUserPlatform.METHOD_LOGIN_EMAIL
                } else if (phone.isNotEmpty()) {
                    method = ApplinkConstInternalUserPlatform.METHOD_LOGIN_PHONE
                }
            }

            bundle.putString(PARAM_LOGIN_METHOD, method)
            bundle.putString(PARAM_PHONE, phone)
            bundle.putString(PARAM_EMAIL, email)
            bundle.putString(PARAM_SOURCE, source)
        }

        return bundle
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putAll(getBundleFromData())
        intent?.extras?.let {
            bundle.putAll(it)
        }

        return LoginEmailPhoneFragmentStub.createInstance(bundle)
    }

    fun setupTestFragment(loginComponentStub: LoginComponentStub) {
        this.loginComponentStub = loginComponentStub
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, TAG)
                .commit()
    }

    override fun getTagFragment(): String {
        return TAG
    }

    companion object {
        const val TAG = "loginfragment"
    }
}