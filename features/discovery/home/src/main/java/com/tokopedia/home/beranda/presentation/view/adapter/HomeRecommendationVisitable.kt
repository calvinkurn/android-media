package com.tokopedia.home.beranda.presentation.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactory

interface HomeRecommendationVisitable : Visitable<HomeRecommendationTypeFactory> {
    fun equalsDataModel(dataModel: Visitable<HomeRecommendationTypeFactory>): Boolean
    fun getUniqueIdentity() : Any
}
