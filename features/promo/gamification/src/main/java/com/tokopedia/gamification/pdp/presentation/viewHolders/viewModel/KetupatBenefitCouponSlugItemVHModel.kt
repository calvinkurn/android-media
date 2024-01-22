package com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponSlugData
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory

class KetupatBenefitCouponSlugItemVHModel(
    val benefitCouponSlugData: KetupatBenefitCouponSlugData.TokopointsCouponListStack.TokopointsCouponDataStack,
    val scratchCard: KetupatLandingPageData.GamiGetScratchCardLandingPage.ScratchCard
) :
    Visitable<KetupatLandingTypeFactory> {
    override fun type(typeFactory: KetupatLandingTypeFactory): Int {
        return typeFactory.type(this)
    }
}
