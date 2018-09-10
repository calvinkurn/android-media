package com.tokopedia.payment.setting.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCreditCardModule
import com.tokopedia.payment.setting.detail.di.DetailCreditCardScope
import com.tokopedia.payment.setting.list.SettingListPaymentFragment
import dagger.Component

@DetailCreditCardScope
@Component(modules = arrayOf(AuthenticateCreditCardModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface SettingListPaymentComponent{
    fun inject(settingListPaymentFragment : SettingListPaymentFragment)
}