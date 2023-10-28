package com.tokopedia.home.beranda.presentation.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

interface HomeRecommendationVisitable : Visitable<HomeRecommendationTypeFactoryImpl> {
    fun equalsDataModel(dataModel: Visitable<HomeRecommendationTypeFactoryImpl>): Boolean
    fun getUniqueIdentity(): Any
}
