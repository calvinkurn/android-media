package com.tokopedia.home.beranda.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import javax.inject.Inject

/**
 * Created by Lukas on 2019-07-16
 */
open class HomeFeedPresenter @Inject constructor(
        private val getHomeFeedUseCase: GetHomeFeedUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase
) : BaseDaggerPresenter<HomeFeedContract.View>(), HomeFeedContract.Presenter{

    fun loadData(recomId: Int, count: Int, page: Int) {
        getHomeFeedUseCase.execute(
                getHomeFeedUseCase.getHomeFeedParam(recomId, count, page),
                GetHomeFeedsSubscriber(view))
    }

    fun addWishlist(model: HomeFeedViewModel, wishlistCallback: (((Boolean, Throwable?) -> Unit))){
        if(model.isTopAds){
            //do special way
        } else {

        }
    }

    fun removeWishlist(model: HomeFeedViewModel, wishlistCallback: (((Boolean, Throwable?) -> Unit))){
        if(model.isTopAds){
            //do special way
        } else {

        }
    }

    override fun detachView() {
        super.detachView()
        getHomeFeedUseCase.unsubscribe()
    }
}