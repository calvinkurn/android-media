package com.tokopedia.gamification.pdp.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory

class C1VHModel(val header: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem) :
    Visitable<KetupatLandingTypeFactory> {
    override fun type(typeFactory: KetupatLandingTypeFactory): Int {
        return typeFactory.type(this)
    }
}
