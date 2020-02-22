package com.tokopedia.home.beranda.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Lukas on 2019-07-16
 */
open class HomeFeedPresenter @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getHomeFeedUseCase: GetHomeFeedUseCase
) : BaseDaggerPresenter<HomeFeedContract.View>(), HomeFeedContract.Presenter{

    fun loadData(recomId: Int, count: Int, page: Int) {
        getHomeFeedUseCase.execute(
                getHomeFeedUseCase.getHomeFeedParam(recomId, count, page),
                GetHomeFeedsSubscriber(view))
    }

    fun isLogin() = userSessionInterface.isLoggedIn

    override fun detachView() {
        super.detachView()
        getHomeFeedUseCase.unsubscribe()
    }
}