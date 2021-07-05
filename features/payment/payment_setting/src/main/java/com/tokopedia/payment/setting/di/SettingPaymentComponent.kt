package com.tokopedia.payment.setting.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.payment.setting.authenticate.view.fragment.AuthenticateCreditCardFragment
import com.tokopedia.payment.setting.detail.view.fragment.DetailCreditCardFragment
import com.tokopedia.payment.setting.list.view.fragment.SettingListPaymentFragment
import dagger.Component

@SettingPaymentScope
@Component(modules = [SettingPaymentModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface SettingPaymentComponent {

    fun inject(settingListPaymentFragment: SettingListPaymentFragment)
    fun inject(detailCreditCardFragment: DetailCreditCardFragment)
    fun inject(authenticateCreditCardFragment: AuthenticateCreditCardFragment)

}