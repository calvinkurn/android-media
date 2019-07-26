package com.tokopedia.home.beranda.presentation.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue



/**
 * Created by Lukas on 2019-07-16
 */
object HomeFeedContract {

    interface Presenter : CustomerPresenter<View>

    interface View : BaseListViewListener<Visitable<HomeFeedTypeFactory>> {
        fun onProductImpression(homeFeedViewModel: HomeFeedViewModel, position: Int)
        fun onProductClick(homeFeedViewModel: HomeFeedViewModel, position: Int)
        fun onWishlistClick(homeFeedViewModel: HomeFeedViewModel, position: Int, isAddWishlist: Boolean, responseWishlist: ((Boolean, Throwable?) -> Unit))
        fun getTrackingQueue(): TrackingQueue
        fun getTabName(): String
    }
}