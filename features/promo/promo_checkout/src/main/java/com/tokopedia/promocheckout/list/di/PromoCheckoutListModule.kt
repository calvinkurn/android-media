package com.tokopedia.promocheckout.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.CheckVoucherDigitalUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.mapper.CheckVoucherDigitalMapper
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDigitalPresenter
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplacePresenter
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListPresenter
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(PromoCheckoutModule::class))
class PromoCheckoutListModule {

    @PromoCheckoutListScope
    @Provides
    fun providePresenter() : PromoCheckoutListPresenter {
        return PromoCheckoutListPresenter(GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideCheckVoucherDigitalUseCase(@ApplicationContext context: Context): CheckVoucherDigitalUseCase {
        return CheckVoucherDigitalUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideMarketplacePresenter(checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase, checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper) : PromoCheckoutListMarketplacePresenter {
        return PromoCheckoutListMarketplacePresenter(checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideDigitalPresenter(checkVoucherDigitalUseCase: CheckVoucherDigitalUseCase, checkVoucherDigitalMapper: CheckVoucherDigitalMapper) : PromoCheckoutListDigitalPresenter {
        return PromoCheckoutListDigitalPresenter(checkVoucherDigitalUseCase, checkVoucherDigitalMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideTrackingPromo(@ApplicationContext context: Context) : TrackingPromoCheckoutUtil{
        return TrackingPromoCheckoutUtil()
    }
}
