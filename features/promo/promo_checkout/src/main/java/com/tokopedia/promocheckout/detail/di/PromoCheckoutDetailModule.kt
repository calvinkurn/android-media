package com.tokopedia.promocheckout.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailDigitalPresenter
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailFlightPresenter
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailHotelPresenter
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailPresenter
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(PromoCheckoutModule::class))
class PromoCheckoutDetailModule {

    @PromoCheckoutDetailScope
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideDigitalCheckVoucherUseCase(@ApplicationContext context: Context): DigitalCheckVoucherUseCase {
        return DigitalCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideFlightCheckVoucherUseCase(@ApplicationContext context: Context): FlightCheckVoucherUseCase {
        return FlightCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideFlightCancelVoucherUseCase(@ApplicationContext context: Context): FlightCancelVoucherUseCase {
        return FlightCancelVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideHotelCheckVoucherUseCase(@ApplicationContext context: Context): HotelCheckVoucherUseCase {
        return HotelCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideMarketplacePresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                         checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase,
                         checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper,
                         clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase): PromoCheckoutDetailPresenter {
        return PromoCheckoutDetailPresenter(getDetailCouponMarketplaceUseCase, checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, clearCacheAutoApplyStackUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideDigitalPresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase,
                                digitalCheckVoucherMapper: DigitalCheckVoucherMapper,
                                clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase): PromoCheckoutDetailDigitalPresenter {
        return PromoCheckoutDetailDigitalPresenter(getDetailCouponMarketplaceUseCase, digitalCheckVoucherUseCase, digitalCheckVoucherMapper, clearCacheAutoApplyStackUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideFlightPresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                               flightCheckVoucherUseCase: FlightCheckVoucherUseCase,
                               flightCheckVoucherMapper: FlightCheckVoucherMapper,
                               flightCancelVoucherUseCase: FlightCancelVoucherUseCase): PromoCheckoutDetailFlightPresenter {
        return PromoCheckoutDetailFlightPresenter(getDetailCouponMarketplaceUseCase, flightCheckVoucherUseCase, flightCheckVoucherMapper, flightCancelVoucherUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideHotelPresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                               hotelCheckVoucherUseCase: HotelCheckVoucherUseCase,
                              hotelCheckVoucherMapper: HotelCheckVoucherMapper): PromoCheckoutDetailHotelPresenter {
        return PromoCheckoutDetailHotelPresenter(getDetailCouponMarketplaceUseCase, hotelCheckVoucherUseCase, hotelCheckVoucherMapper)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideGetDetailMarketplaceUseCase(@ApplicationContext context: Context): GetDetailCouponMarketplaceUseCase {
        return GetDetailCouponMarketplaceUseCase(context.resources)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideTrackingPromo(@ApplicationContext context: Context): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }
}
