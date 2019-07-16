package com.tokopedia.promocheckout.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.CheckVoucherDigitalUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.mapper.CheckVoucherDigitalMapper
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailDigitalPresenter
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
    fun provideCheckVoucherDigitalUseCase(@ApplicationContext context: Context): CheckVoucherDigitalUseCase {
        return CheckVoucherDigitalUseCase(context.resources)
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
                                checkVoucherDigitalUseCase: CheckVoucherDigitalUseCase,
                                checkVoucherDigitalMapper: CheckVoucherDigitalMapper,
                                clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase): PromoCheckoutDetailDigitalPresenter {
        return PromoCheckoutDetailDigitalPresenter(getDetailCouponMarketplaceUseCase, checkVoucherDigitalUseCase, checkVoucherDigitalMapper, clearCacheAutoApplyStackUseCase)
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
