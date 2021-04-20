package com.tokopedia.loginregister.login.stub.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginregister.login.stub.LoginEmailPhoneFragmentStub
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yoris Prayogo on 05/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class LoginEmailPhoneActivityStub: LoginActivity() {
    lateinit var stubUserSession: UserSessionInterface
    lateinit var stubFingerprintSetting: FingerprintSetting

    override fun onCreate(savedInstanceState: Bundle?) {
        stubUserSession = UserSession(this)
        stubFingerprintSetting = FingerprintPreferenceHelper(this)
        super.onCreate(savedInstanceState)
    }

    fun setupTestFragment(): Fragment {
        val newFragment = newFragment
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, tagFragment)
                .commit()
        return newFragment
    }

    fun setupTestFragmentAllowingLossState(): Fragment {
        val newFragment = newFragment
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, tagFragment)
                .commitAllowingStateLoss()
        return newFragment
    }

    override fun getNewFragment(): Fragment {
        return LoginEmailPhoneFragmentStub.createFragment(
                stubUserSession,
                stubFingerprintSetting
        )
    }
}