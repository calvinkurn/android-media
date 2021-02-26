package com.tokopedia.loginregister.login.stub

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.login.dagger.DaggerMockLoginComponent
import com.tokopedia.loginregister.login.dagger.MockLoginQueryModule
import com.tokopedia.loginregister.login.dagger.MockLoginUseCaseModule
import com.tokopedia.loginregister.login.di.LoginModule
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yoris Prayogo on 05/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class LoginEmailPhoneFragmentStub : LoginEmailPhoneFragment(){

    lateinit var stubUserSession: UserSessionInterface
    lateinit var stubFingerprintSetting: FingerprintSetting


    fun setFingerprintEnable(enable: Boolean) {
        isEnableFingerprint = enable
    }

    override fun routeToVerifyPage(phoneNumber: String, requestCode: Int, otpType: Int) {
        // do nothing
    }

    override fun goToForgotPassword(){
        // do nothing
    }

    override fun goToRegisterInitial(source: String) {
        // do nothing
    }

    override fun openGoogleLoginIntent() {
        //do nothing
    }

    override fun initInjector() {
        DaggerMockLoginComponent.builder()
                .loginRegisterComponent(DaggerLoginRegisterComponent.builder()
                        .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                        .build())
                .mockLoginQueryModule(MockLoginQueryModule())
                .mockLoginUseCaseModule(MockLoginUseCaseModule())
                .loginModule(object: LoginModule(){
                    override fun provideCryptographyUtils(): Cryptography? {
                        return MockCryptography()
                    }
                })
                .build()
                .inject(this)
    }

    companion object {
            fun createFragment(
                    userSessionInterface: UserSessionInterface,
                    fingerprintSetting: FingerprintSetting,
                    stubEnableFingerprint: Boolean = false
            ): LoginEmailPhoneFragment {
                return LoginEmailPhoneFragmentStub().apply {
                    stubUserSession = userSessionInterface
                    stubFingerprintSetting = fingerprintSetting
                    isEnableFingerprint = stubEnableFingerprint
                }
            }
        }
}