package com.tokopedia.shop.favourite.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FAVORITE_LIST_SOURCE
import com.tokopedia.shop.favourite.data.pojo.shopfollowerlist.GetShopFollowerListData
import com.tokopedia.shop.favourite.data.pojo.shopfollowerlist.ShopFollowerData
import com.tokopedia.shop.favourite.domain.interactor.GetShopFollowerListUseCase
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView
import com.tokopedia.shop.favourite.view.model.ShopFollowerListResultUiModel
import com.tokopedia.shop.favourite.view.model.ShopFollowerUiModel
import com.tokopedia.shop.pageheader.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import rx.Subscriber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.max

/**
 * Created by nathan on 2/6/18.
 */
class ShopFavouriteListPresenter @Inject
constructor(private val getShopFollowerListUseCase: GetShopFollowerListUseCase,
            private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase,
            private val userSession: UserSessionInterface,
            private val toggleFavouriteShopAndDeleteCacheUseCase: ToggleFavouriteShopAndDeleteCacheUseCase,
            private val dispatchers: CoroutineDispatchers) : BaseDaggerPresenter<ShopFavouriteListView>(), CoroutineScope {

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
        gqlGetShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(listOf(shopId.toIntOrZero()), source = FAVORITE_LIST_SOURCE)
        gqlGetShopInfoUseCase.execute(
                {
                    if(isViewAttached) {
                        view?.onSuccessGetShopInfo(it)
                    }
                },
                {
                    if(isViewAttached) {
                        view.showGetListError(it)
                    }
                }
        )
    }

    override fun detachView() {
        super.detachView()
        toggleFavouriteShopAndDeleteCacheUseCase.unsubscribe()
        gqlGetShopInfoUseCase.cancelJobs()
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