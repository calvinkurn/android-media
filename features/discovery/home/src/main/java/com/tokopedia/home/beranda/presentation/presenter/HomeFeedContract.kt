package com.tokopedia.home.beranda.presentation.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.trackingoptimizer.TrackingQueue



/**
 * Created by Lukas on 2019-07-16
 */
object HomeFeedContract {

    interface Presenter : CustomerPresenter<View>

    interface View : BaseListViewListener<HomeRecommendationVisitable> {
        fun onProductImpression(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
        fun onProductClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
        fun onProductThreeDotsClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
        fun getTrackingQueue(): TrackingQueue
        fun getTabName(): String
    }
}