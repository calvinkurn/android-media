package com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory

class KetupatRedirectionBannerVHModel(val redirectionBanner: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem,
    val scratchCard: KetupatLandingPageData.GamiGetScratchCardLandingPage.ScratchCard) :
    Visitable<KetupatLandingTypeFactory> {
    override fun type(typeFactory: KetupatLandingTypeFactory): Int {
        return typeFactory.type(this)
    }
}
