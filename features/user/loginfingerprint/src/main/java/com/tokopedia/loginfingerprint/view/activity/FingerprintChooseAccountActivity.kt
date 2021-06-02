package com.tokopedia.loginfingerprint.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginfingerprint.di.DaggerLoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintSettingModule
import com.tokopedia.loginfingerprint.view.fragment.ChooseAccountFingerprintFragment
import com.tokopedia.loginphone.chooseaccount.view.activity.ChooseAccountActivity

class FingerprintChooseAccountActivity: BaseSimpleActivity(), HasComponent<LoginFingerprintComponent> {

    override fun getScreenName(): String {
        return ""
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null)
            bundle.putAll(intent.extras)
        return ChooseAccountFingerprintFragment.createInstance(bundle)
    }

    override fun getComponent(): LoginFingerprintComponent {
        return DaggerLoginFingerprintComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .loginFingerprintSettingModule(LoginFingerprintSettingModule(this))
            .build()
    }
}