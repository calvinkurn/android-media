package com.tokopedia.payment.setting.detail.di

import com.tokopedia.payment.setting.detail.view.presenter.DetailCreditCardPresenter
import dagger.Module
import dagger.Provides

@Module
class DetailCreditCardModule {

    @DetailCreditCardScope
    @Provides
    fun providePresenter() : DetailCreditCardPresenter {
        return DetailCreditCardPresenter()
    }
}
