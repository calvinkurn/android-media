package com.tokopedia.loginregister.login.behaviour.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterEmailFragment

class RegisterEmailFragmentStub: RegisterEmailFragment() {
    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = RegisterEmailFragmentStub()
            fragment.arguments = bundle
            return fragment
        }
    }
}