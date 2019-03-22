package com.tokopedia.promocheckout.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutRouter
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingFirstCodeMapper
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

    /*@PromoCheckoutListScope
    @Provides
    fun provideMarketplacePresenter(@PromoCheckoutQualifier checkPromoCodeUseCase: CheckPromoCodeUseCase) : PromoCheckoutListMarketplacePresenter {
        return PromoCheckoutListMarketplacePresenter(checkPromoCodeUseCase)
    }*/

    @PromoCheckoutListScope
    @Provides
    fun provideMarketplacePresenter(@PromoCheckoutQualifier checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase, checkPromoStackingCodeMapper: CheckPromoStackingFirstCodeMapper) : PromoCheckoutListMarketplacePresenter {
        return PromoCheckoutListMarketplacePresenter(checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideTrackingPromo(@ApplicationContext context: Context) : TrackingPromoCheckoutUtil{
        if(context is TrackingPromoCheckoutRouter){
            return TrackingPromoCheckoutUtil(context)
        }else{
            return TrackingPromoCheckoutUtil(null)
        }
    }
}
