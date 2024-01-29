package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

interface BaseHomeRecommendationVisitable : Visitable<HomeRecommendationTypeFactoryImpl> {
    fun areItemsTheSame(other: Any): Boolean

    fun areContentsTheSame(other: Any): Boolean
}
