package com.tokopedia.payment.setting.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.detail.di.DetailCreditCardScope
import com.tokopedia.payment.setting.list.view.fragment.SettingListPaymentFragment
import dagger.Component

@DetailCreditCardScope
@Component(modules = arrayOf(SettingListPaymentModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface SettingListPaymentComponent{
    fun inject(settingListPaymentFragment : SettingListPaymentFragment)
}