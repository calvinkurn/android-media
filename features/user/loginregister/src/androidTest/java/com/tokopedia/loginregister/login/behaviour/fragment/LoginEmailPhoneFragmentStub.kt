package com.tokopedia.loginregister.login.behaviour.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.login.behaviour.activity.ChangeNameActivityStub
import com.tokopedia.loginregister.login.behaviour.activity.ChooseAccountActivityStub
import com.tokopedia.loginregister.login.behaviour.activity.VerificationActivityStub
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment

class LoginEmailPhoneFragmentStub: LoginEmailPhoneFragment() {

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginEmailPhoneFragmentStub()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun goToChooseAccountPage(accessToken: String, phoneNumber: String) {
        val intent = Intent(activity, ChooseAccountActivityStub::class.java)
        startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
    }

    override fun goToLoginPhoneVerifyPage(phoneNumber: String) {
        startActivityForResult(Intent(activity, VerificationActivityStub::class.java), REQUEST_LOGIN_PHONE)
    }

    override fun onGoToChangeName() {
        startActivity(Intent(activity, ChangeNameActivityStub::class.java))
    }

    override fun refreshRolloutVariant() {
        // do nothing
    }

    override fun isEnableEncryption(): Boolean = true
}