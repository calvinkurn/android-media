package com.tokopedia.payment.setting.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.list.SettingListPaymentFragment
import dagger.Component

@DetailCreditCardScope
@Component(modules = arrayOf(DetailCreditCardModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface DetailCreditCardComponent{
    fun inject(settingListPaymentFragment : SettingListPaymentFragment)
}