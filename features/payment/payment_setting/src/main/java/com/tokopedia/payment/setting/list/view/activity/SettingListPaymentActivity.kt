package com.tokopedia.payment.setting.list.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.payment.setting.list.di.DaggerSettingListPaymentComponent
import com.tokopedia.payment.setting.list.di.SettingListPaymentComponent
import com.tokopedia.payment.setting.list.di.SettingListPaymentModule
import com.tokopedia.payment.setting.list.view.fragment.SettingListPaymentFragment

class SettingListPaymentActivity : BaseSimpleActivity(), HasComponent<SettingListPaymentComponent>{

    override fun getNewFragment(): Fragment {
        return SettingListPaymentFragment.createInstance()
    }

    override fun getComponent(): SettingListPaymentComponent {
        return DaggerSettingListPaymentComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .settingListPaymentModule(SettingListPaymentModule(this))
                .build()
    }


}