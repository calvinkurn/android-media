package com.tokopedia.gamification.pdp.presentation.adapters

import com.tokopedia.gamification.pdp.data.C1VHModel
import com.tokopedia.gamification.pdp.data.Recommendation


interface KetupatLandingTypeFactory{

    fun type(model: C1VHModel): Int
}
