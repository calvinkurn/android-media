package com.tokopedia.gamification.pdp.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapterTypeFactory

class C1VHModel(/*add the data that need to put in this class*/) :
    Visitable<KetupatLandingAdapterTypeFactory> {
    override fun type(typeFactory: KetupatLandingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
