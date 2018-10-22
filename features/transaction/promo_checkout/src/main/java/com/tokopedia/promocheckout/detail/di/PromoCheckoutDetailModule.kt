package com.tokopedia.promocheckout.detail.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.detail.PromoCheckoutDetailPresenter
import com.tokopedia.promocheckout.list.PromoCheckoutListPresenter
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
