package com.tokopedia.promocheckout.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier
import com.tokopedia.promocheckout.common.domain.CancelPromoUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase
import com.tokopedia.promocheckout.common.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailPresenter
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(PromoCheckoutModule::class))
class PromoCheckoutDetailModule {

    @PromoCheckoutDetailScope
    @Provides
    fun providePresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                         @PromoCheckoutQualifier checkPromoCodeUseCase: CheckPromoCodeUseCase,
                         @PromoCheckoutQualifier cancelPromoUseCase: CancelPromoUseCase): PromoCheckoutDetailPresenter {
        return PromoCheckoutDetailPresenter(getDetailCouponMarketplaceUseCase, checkPromoCodeUseCase, cancelPromoUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideGetDetailMarketplaceUseCase(@ApplicationContext context: Context, @PromoCheckoutQualifier checkPromoCodeUseCase: CheckPromoCodeUseCase): GetDetailCouponMarketplaceUseCase {
        return GetDetailCouponMarketplaceUseCase(context.resources, GraphqlUseCase(), checkPromoCodeUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideTrackingPromo(@ApplicationContext context: Context): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }
}
