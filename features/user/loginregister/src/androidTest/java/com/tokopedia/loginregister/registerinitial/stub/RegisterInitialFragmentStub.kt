package com.tokopedia.loginregister.registerinitial.stub

import android.os.Bundle
import android.util.Log
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment

class RegisterInitialFragmentStub : RegisterInitialFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initInjector() {
        super.initInjector()
    }

    override fun goToRegisterEmailPage() {
        Log.d("NOTHING", "DO NOTHING")
        //stub, do nothing
    }

    override fun goToRegisterGoogle() {
        Log.d("NOTHING", "DO NOTHING")
        //stub, do nothing
    }

    override fun goToRegisterFacebook() {
        Log.d("NOTHING", "DO NOTHING")
        //stub, do nothing
    }

    override fun goToLoginPage() {
        Log.d("NOTHING", "DO NOTHING")
        //stub, do nothing
    }

    override fun goToLoginRegisteredPhoneNumber(phone: String) {
        Log.d("NOTHING", "DO NOTHING")
        //stub, do nothing
    }

    override fun goToRegisterWithPhoneNumber(phone: String){
        Log.d("NOTHING", "DO NOTHING")
        //stub, do nothing
    }

    override fun goToOTPActivateEmail(phone: String){
        Log.d("NOTHING", "DO NOTHING")
        //stub, do nothing
    }

    override fun goToOTPRegisterEmail(phone: String){
        Log.d("NOTHING", "DO NOTHING")
        //stub, do nothing
    }
}