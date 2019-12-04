package com.tokopedia.promocheckout.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDigitalPresenter
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListFlightPresenter
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplacePresenter
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListPresenter
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
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources)
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
    fun provideMarketplacePresenter(checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase, checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper, getCatalogHighlightUseCase: GraphqlUseCase): PromoCheckoutListMarketplacePresenter {
        return PromoCheckoutListMarketplacePresenter(checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, getCatalogHighlightUseCase)
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
    fun provideTrackingPromo(@ApplicationContext context: Context): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }
}
