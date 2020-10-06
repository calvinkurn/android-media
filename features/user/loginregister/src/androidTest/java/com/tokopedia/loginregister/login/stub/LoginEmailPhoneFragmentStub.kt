package com.tokopedia.loginregister.login.stub

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.dagger.DaggerMockLoginComponent
import com.tokopedia.loginregister.login.dagger.MockLoginQueryModule
import com.tokopedia.loginregister.login.dagger.MockLoginUseCaseModule
import com.tokopedia.loginregister.login.dagger.MockLoginmodule
import com.tokopedia.loginregister.login.di.DaggerLoginComponent
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yoris Prayogo on 05/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class LoginEmailPhoneFragmentStub : LoginEmailPhoneFragment(){

    lateinit var stubUserSession: UserSessionInterface
    lateinit var stubFingerprintSetting: FingerprintSetting

    //    lateinit var chatNotificationUseCase: GetChatNotificationUseCaseStub

    fun setFingerprintEnable(enable: Boolean) {
        isEnableFingerprint = enable
    }

    override fun initInjector() {
        DaggerMockLoginComponent.builder()
                .loginRegisterComponent(DaggerLoginRegisterComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build())
                .mockLoginQueryModule(MockLoginQueryModule())
                .mockLoginUseCaseModule(MockLoginUseCaseModule())
                .mockLoginmodule(MockLoginmodule())
                .build()
                .inject(this)
        presenter.attachView(this, this)
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