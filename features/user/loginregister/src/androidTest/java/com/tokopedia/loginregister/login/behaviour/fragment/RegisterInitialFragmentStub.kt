package com.tokopedia.loginregister.login.behaviour.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment

class RegisterInitialFragmentStub: RegisterInitialFragment() {
    override fun fetchRemoteConfig() {
        // DO NOTHING
    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = RegisterInitialFragmentStub()
            fragment.arguments = bundle
            return fragment
        }
    }


}