package com.tokopedia.promocheckout.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.mapper.UmrahCheckPromoMapper
import com.tokopedia.promocheckout.common.domain.umroh.UmrahCheckPromoUseCase
import com.tokopedia.promocheckout.list.view.presenter.*
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(PromoCheckoutModule::class))
class PromoCheckoutListModule {

    @PromoCheckoutListScope
    @Provides
    fun providePresenter(): PromoCheckoutListPresenter {
        return PromoCheckoutListPresenter(GraphqlUseCase(), GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideDigitalCheckVoucherUseCase(@ApplicationContext context: Context): DigitalCheckVoucherUseCase {
        return DigitalCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideFlightCheckVoucherUseCase(@ApplicationContext context: Context): FlightCheckVoucherUseCase {
        return FlightCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideHotelCheckVoucherUseCase(@ApplicationContext context: Context): HotelCheckVoucherUseCase {
        return HotelCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideUmrahCheckPromoUseCase(@ApplicationContext context: Context): UmrahCheckPromoUseCase {
        return UmrahCheckPromoUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideMarketplacePresenter(graphqlUseCase: GraphqlUseCase, checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase): PromoCheckoutListMarketplacePresenter {
        return PromoCheckoutListMarketplacePresenter(graphqlUseCase, checkPromoStackingCodeUseCase)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideDigitalPresenter(digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase, digitalCheckVoucherMapper: DigitalCheckVoucherMapper): PromoCheckoutListDigitalPresenter {
        return PromoCheckoutListDigitalPresenter(digitalCheckVoucherUseCase, digitalCheckVoucherMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideFlightPresenter(flightCheckVoucherUseCase: FlightCheckVoucherUseCase,
                               flightCheckVoucherMapper: FlightCheckVoucherMapper): PromoCheckoutListFlightPresenter {
        return PromoCheckoutListFlightPresenter(flightCheckVoucherUseCase, flightCheckVoucherMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideHotelPresenter(hotelCheckVoucherUseCase: HotelCheckVoucherUseCase,
                              hotelCheckVoucherMapper: HotelCheckVoucherMapper) : PromoCheckoutListHotelPresenter {
        return PromoCheckoutListHotelPresenter(hotelCheckVoucherUseCase, hotelCheckVoucherMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideUmrahPresenter(umrahCheckPromoUseCase: UmrahCheckPromoUseCase,
                              umrahCheckPromoMapper: UmrahCheckPromoMapper) : PromoCheckoutListUmrahPresenter {
        return PromoCheckoutListUmrahPresenter(umrahCheckPromoUseCase, umrahCheckPromoMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideEventPresenter():PromoCheckoutListEventPresenter{
        return PromoCheckoutListEventPresenter()
    }

    @PromoCheckoutListScope
    @Provides
    fun provideTrackingPromo(@ApplicationContext context: Context): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }
}
