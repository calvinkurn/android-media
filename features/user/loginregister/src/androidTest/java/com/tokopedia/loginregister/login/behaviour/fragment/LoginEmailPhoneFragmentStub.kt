package com.tokopedia.loginregister.login.behaviour.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment

class LoginEmailPhoneFragmentStub: LoginEmailPhoneFragment() {

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginEmailPhoneFragmentStub()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun refreshRolloutVariant() {
        // do nothing
    }

    override fun isEnableEncryption(): Boolean = true
}