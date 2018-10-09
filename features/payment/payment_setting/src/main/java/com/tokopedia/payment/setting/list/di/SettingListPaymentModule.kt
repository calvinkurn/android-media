package com.tokopedia.payment.setting.list.di

import com.tokopedia.payment.setting.detail.di.DetailCreditCardScope
import com.tokopedia.payment.setting.list.view.presenter.SettingListPaymentPresenter
import dagger.Module
import dagger.Provides

@Module
class SettingListPaymentModule {

    @DetailCreditCardScope
    @Provides
    fun providePresenter() : SettingListPaymentPresenter {
        return SettingListPaymentPresenter()
    }
}
