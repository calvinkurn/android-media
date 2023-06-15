package com.tokopedia.recharge_credit_card.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.recharge_credit_card.RechargeCCActivity
import com.tokopedia.recharge_credit_card.bottomsheet.CCBankListBottomSheet
import com.tokopedia.recharge_credit_card.RechargeCCFragment
import com.tokopedia.recharge_credit_card.RechargeCCPromoFragment
import com.tokopedia.recharge_credit_card.RechargeCCRecommendationFragment
import dagger.Component

@RechargeCCScope
@Component(modules = [RechargeCCModule::class, RechargeCCViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface RechargeCCComponent {

    fun inject(CCBankListBottomSheet: CCBankListBottomSheet)

    fun inject(rechargeCCFragment: RechargeCCFragment)

    fun inject(rechargeCCActivity: RechargeCCActivity)

    fun inject(rechargeCCRecommendationFragment: RechargeCCRecommendationFragment)

    fun inject(rechargeCCPromoFragment: RechargeCCPromoFragment)

}
