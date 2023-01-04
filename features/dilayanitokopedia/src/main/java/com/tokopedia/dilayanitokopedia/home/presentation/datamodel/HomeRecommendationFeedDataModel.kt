package com.tokopedia.dilayanitokopedia.home.presentation.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeTypeFactory

data class HomeRecommendationFeedDataModel(
    val removelater: String = ""
) : Visitable<HomeTypeFactory> {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
