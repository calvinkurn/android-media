package com.tokopedia.loginregister.registerinitial.stub

import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment

class RegisterInitialFragmentStub : RegisterInitialFragment() {

    override fun goToRegisterEmailPage() {
        //stub, do nothing
    }

    override fun goToRegisterGoogle() {
        registerInitialViewModel.registerGoogle("dummyAccessToken", "dummyEmail")
    }

    override fun goToRegisterFacebook() {
        registerInitialViewModel.registerFacebook("dummyAccessToken", "dummyEmail")
    }

    override fun goToLoginPage() {
        //stub, do nothing
    }

    override fun goToLoginRegisteredPhoneNumber(phone: String) {
        //stub, do nothing
    }

    override fun goToOTPActivateEmail(email: String){
        //stub, do nothing
    }

    override fun onSuccessRegister(){
        //sub, do nothing
    }
}