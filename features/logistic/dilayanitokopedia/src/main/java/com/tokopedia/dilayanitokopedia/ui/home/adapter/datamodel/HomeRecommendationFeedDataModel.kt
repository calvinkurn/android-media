package com.tokopedia.dilayanitokopedia.ui.home.adapter.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.common.view.adapter.typefactory.HomeTypeFactory

data class HomeRecommendationFeedDataModel(
    val removelater: String = ""
) : Visitable<HomeTypeFactory> {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
