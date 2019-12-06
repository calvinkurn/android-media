package com.tokopedia.shop.favourite.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.feedcomponent.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase
import com.tokopedia.shop.favourite.data.pojo.shopfollowerlist.GetShopFollowerListData
import com.tokopedia.shop.favourite.data.pojo.shopfollowerlist.ShopFollowerData
import com.tokopedia.shop.favourite.domain.interactor.GetShopFollowerListUseCase
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView
import com.tokopedia.shop.favourite.view.model.ShopFollowerUiModel
import com.tokopedia.shop.favourite.view.model.ShopFollowerListResultUiModel
import com.tokopedia.shop.oldpage.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*

import javax.inject.Inject

import rx.Subscriber
import kotlin.coroutines.CoroutineContext
import kotlin.math.max

/**
 * Created by nathan on 2/6/18.
 */
class ShopFavouriteListPresenter @Inject
constructor(private val getShopFollowerListUseCase: GetShopFollowerListUseCase,
            private val getShopInfoUseCase: GetShopInfoUseCase,
            private val userSession: UserSessionInterface,
            private val toggleFavouriteShopAndDeleteCacheUseCase: ToggleFavouriteShopAndDeleteCacheUseCase,
            private val dispatchers: CoroutineDispatcherProvider) : BaseDaggerPresenter<ShopFavouriteListView>(), CoroutineScope {

    //region CoroutineScope
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    //endregion

    val isLoggedIn: Boolean
        get() = userSession.isLoggedIn

    fun getShopFavouriteList(shopId: String, page: Int) {
        val thePage = max(1, page)
        launch {
            try {
                val data = getShopFollowerListUseCase.apply {
                    clearRequest()
                    addRequestWithParam(shopId, thePage)
                }.executeOnBackground().convertToUiModel(thePage)
                view?.renderList(data.followerUiModelList, data.isCanLoadMore)
            } catch (e: Throwable) {
                view?.showGetListError(e)
            }
        }
    }

    fun toggleFavouriteShop(shopId: String) {
        if (!userSession.isLoggedIn) {
            view.onErrorToggleFavourite(UserNotLoginException())
            return
        }
        toggleFavouriteShopAndDeleteCacheUseCase.execute(
                ToggleFavouriteShopAndDeleteCacheUseCase.createRequestParam(shopId), object : Subscriber<Boolean>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.onErrorToggleFavourite(e)
                }
            }

            override fun onNext(aBoolean: Boolean?) {
                view.onSuccessToggleFavourite(aBoolean!!)
            }
        })
    }

    fun isMyShop(shopId: String): Boolean {
        return userSession.shopId == shopId
    }

    fun getShopInfo(shopId: String) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showGetListError(e)
                }
            }

            override fun onNext(shopInfo: ShopInfo) {
                view.onSuccessGetShopInfo(shopInfo)
            }
        })
    }

    override fun detachView() {
        super.detachView()
        toggleFavouriteShopAndDeleteCacheUseCase.unsubscribe()
        getShopInfoUseCase.unsubscribe()
        job.cancel()
    }

    private fun GetShopFollowerListData.convertToUiModel(currentPage: Int) = shopFollowerList.let { result ->
        ShopFollowerListResultUiModel(
                isCanLoadMore = result.haveNext,
                currentPage = currentPage,
                followerUiModelList = result.shopFollowerData.map { it.convertToUiModel() }
        )
    }

    private fun ShopFollowerData.convertToUiModel(): ShopFollowerUiModel = ShopFollowerUiModel(
            id = followerID,
            name = followerName,
            imageUrl = photo,
            profileUrl = profileURL
    )
}