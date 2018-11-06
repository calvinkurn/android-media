package com.tokopedia.promocheckout.detail.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailPresenter
import dagger.Module
import dagger.Provides

@Module
class PromoCheckoutDetailModule {

    @PromoCheckoutDetailScope
    @Provides
    fun providePresenter() : PromoCheckoutDetailPresenter {
        return PromoCheckoutDetailPresenter(GraphqlUseCase())
    }
}
