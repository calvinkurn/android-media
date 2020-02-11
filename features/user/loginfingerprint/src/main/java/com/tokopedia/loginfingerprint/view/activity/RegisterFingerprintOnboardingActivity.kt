package com.tokopedia.loginfingerprint.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.di.DaggerLoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintComponent
import com.tokopedia.loginfingerprint.view.fragment.RegisterFingerprintOnboardingFragment

class RegisterFingerprintOnboardingActivity : BaseSimpleActivity(), HasComponent<LoginFingerprintComponent>{

    override fun getComponent(): LoginFingerprintComponent {
        return DaggerLoginFingerprintComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun getNewFragment(): Fragment? {
        return RegisterFingerprintOnboardingFragment()
    }
}
