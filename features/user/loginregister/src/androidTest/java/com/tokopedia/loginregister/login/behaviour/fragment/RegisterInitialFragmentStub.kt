package com.tokopedia.loginregister.login.behaviour.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.login.behaviour.activity.LoginActivityStub
import com.tokopedia.loginregister.login.behaviour.activity.VerificationActivityStub
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment

class RegisterInitialFragmentStub: RegisterInitialFragment() {
    override fun fetchRemoteConfig() {
        // DO NOTHING
    }

    override fun goToLoginPage() {
        startActivity(Intent(context, LoginActivityStub::class.java))
    }

    override fun goToOTPRegisterEmail(email: String) {
        startActivity(Intent(context, VerificationActivityStub::class.java))
    }

    override fun gotoLoginEmailPage(email: String) {
        startActivity(Intent(context, LoginActivityStub::class.java))
    }

    override fun goToLoginRegisteredPhoneNumber(phone: String) {
        startActivity(Intent(context, LoginActivityStub::class.java))
    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = RegisterInitialFragmentStub()
            fragment.arguments = bundle
            return fragment
        }
    }


}