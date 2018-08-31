package com.tokopedia.payment.setting.list.di

import com.tokopedia.payment.setting.list.SettingListPaymentPresenter
import dagger.Module
import dagger.Provides

@Module
class SettingListPaymentModule {

    @SettingListPaymentScope
    @Provides
    fun providePresenter() : SettingListPaymentPresenter{
        return SettingListPaymentPresenter()
    }
}
