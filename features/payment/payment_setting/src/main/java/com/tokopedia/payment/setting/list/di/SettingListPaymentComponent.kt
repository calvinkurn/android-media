package com.tokopedia.payment.setting.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.list.SettingListPaymentFragment
import dagger.Component

@SettingListPaymentScope
@Component(modules = arrayOf(SettingListPaymentModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface SettingListPaymentComponent{
    fun inject(settingListPaymentFragment : SettingListPaymentFragment)
}