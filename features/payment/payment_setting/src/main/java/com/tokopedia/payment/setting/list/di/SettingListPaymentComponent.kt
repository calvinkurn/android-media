package com.tokopedia.payment.setting.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.list.view.fragment.SettingListPaymentFragment
import dagger.Component

@SettingListPaymentScope
@Component(modules = [SettingListPaymentModule::class,
    GqlQueryModule::class], dependencies = [BaseAppComponent::class])
interface SettingListPaymentComponent{
    fun inject(settingListPaymentFragment : SettingListPaymentFragment)
}