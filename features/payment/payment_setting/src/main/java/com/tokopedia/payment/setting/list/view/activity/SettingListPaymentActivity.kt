package com.tokopedia.payment.setting.list.view.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.payment.setting.di.DaggerSettingPaymentComponent
import com.tokopedia.payment.setting.di.SettingPaymentComponent
import com.tokopedia.payment.setting.di.SettingPaymentModule
import com.tokopedia.payment.setting.list.view.fragment.SettingListPaymentFragment

class SettingListPaymentActivity : BaseSimpleActivity(), HasComponent<SettingPaymentComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
    }

    private fun setSecureWindowFlag() {
        if(GlobalConfig.APPLICATION_TYPE== GlobalConfig.CONSUMER_APPLICATION|| GlobalConfig.APPLICATION_TYPE== GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread {
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

    override fun getNewFragment(): Fragment {
        return SettingListPaymentFragment.createInstance()
    }

    override fun getComponent(): SettingPaymentComponent {
        return DaggerSettingPaymentComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .settingPaymentModule(SettingPaymentModule(this))
                .build()
    }


}