package com.tokopedia.promocheckout.detail.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier
import com.tokopedia.promocheckout.common.domain.CancelPromoUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailPresenter
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(PromoCheckoutModule::class))
class PromoCheckoutDetailModule {

    @PromoCheckoutDetailScope
    @Provides
    fun providePresenter(@PromoCheckoutQualifier checkPromoCodeUseCase: CheckPromoCodeUseCase, @PromoCheckoutQualifier cancelPromoUseCase: CancelPromoUseCase) : PromoCheckoutDetailPresenter {
        return PromoCheckoutDetailPresenter(GraphqlUseCase(), checkPromoCodeUseCase, cancelPromoUseCase)
    }
}
