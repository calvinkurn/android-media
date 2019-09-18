package com.tokopedia.home.beranda.presentation.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.FeedTabModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

/**
 * Created by devarafikry on 27/08/19.
 */

class HomeRecommendationFeedViewModel : Visitable<HomeTypeFactory> {

    var feedTabModel: List<FeedTabModel>? = null

    var isNewData = true

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
