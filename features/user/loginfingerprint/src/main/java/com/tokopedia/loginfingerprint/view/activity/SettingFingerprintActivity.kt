package com.tokopedia.loginfingerprint.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginfingerprint.di.DaggerLoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintSettingModule
import com.tokopedia.loginfingerprint.view.fragment.SettingFingerprintFragment

class SettingFingerprintActivity: BaseSimpleActivity(), HasComponent<LoginFingerprintComponent> {

    override fun getScreenName(): String {
        return ""
    }

    override fun getComponent(): LoginFingerprintComponent {
        return DaggerLoginFingerprintComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .loginFingerprintSettingModule(LoginFingerprintSettingModule(this))
            .build()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null)
            bundle.putAll(intent.extras)
        return SettingFingerprintFragment.createInstance(bundle)
    }
}