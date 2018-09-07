package com.tokopedia.payment.setting.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.list.SettingListPaymentFragment
import com.tokopedia.payment.setting.add.di.AddCreditCardModule
import com.tokopedia.payment.setting.detail.DetailCreditCardFragment
import dagger.Component

@DetailCreditCardScope
@Component(modules = arrayOf(AddCreditCardModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface DetailCreditCardComponent{
    fun inject(detailCreditCardFragment: DetailCreditCardFragment)
}