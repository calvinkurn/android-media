package com.tokopedia.payment.setting.list.di

import com.tokopedia.payment.setting.detail.DetailCreditCardPresenter
import dagger.Module
import dagger.Provides

@Module
class DetailCreditCardModule {

    @DetailCreditCardScope
    @Provides
    fun providePresenter() : DetailCreditCardPresenter{
        return DetailCreditCardPresenter()
    }
}
