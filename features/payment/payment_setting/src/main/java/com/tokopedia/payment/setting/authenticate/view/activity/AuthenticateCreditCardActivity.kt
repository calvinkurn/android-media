package com.tokopedia.payment.setting.authenticate.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.payment.setting.authenticate.view.fragment.AuthenticateCreditCardFragment
import com.tokopedia.payment.setting.di.DaggerSettingPaymentComponent
import com.tokopedia.payment.setting.di.SettingPaymentComponent
import com.tokopedia.payment.setting.di.SettingPaymentModule

class AuthenticateCreditCardActivity : BaseSimpleActivity(), HasComponent<SettingPaymentComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
    }

    private fun setSecureWindowFlag() {
        runOnUiThread { window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) }
    }

    override fun getNewFragment(): Fragment {
        return AuthenticateCreditCardFragment.createInstance()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AuthenticateCreditCardActivity::class.java)
        }
    }

    override fun getComponent(): SettingPaymentComponent {
        return DaggerSettingPaymentComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .settingPaymentModule(SettingPaymentModule(this))
                .build()
    }
}