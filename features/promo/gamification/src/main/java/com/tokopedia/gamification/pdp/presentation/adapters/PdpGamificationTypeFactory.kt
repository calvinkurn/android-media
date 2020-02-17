package com.tokopedia.gamification.pdp.presentation.adapters

import com.tokopedia.gamification.pdp.data.Recommendation


interface PdpGamificationTypeFactory{

    fun type(recomendation: Recommendation): Int
}