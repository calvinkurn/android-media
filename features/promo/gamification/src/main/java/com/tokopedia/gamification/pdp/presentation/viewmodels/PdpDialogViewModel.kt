package com.tokopedia.gamification.pdp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.data.di.modules.DispatcherModule
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class PdpDialogViewModel @Inject constructor(private val recommendationProductUseCase: GamingRecommendationProductUseCase,
                                             private val addWishListUseCase: AddToWishlistV2UseCase,
                                             private val removeWishListUseCase: DeleteWishlistV2UseCase,
                                             private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
                                             val userSession: UserSessionInterface,
                                             @Named(DispatcherModule.IO) val workerDispatcher: CoroutineDispatcher) : BaseViewModel(workerDispatcher) {

    val productLiveData: MutableLiveData<LiveDataResult<List<Recommendation>>> = MutableLiveData()

    var shopId = ""
    var pageName = ""
    var useEmptyShopId = false

    fun getProducts(pageNumber: Int) {
        launchCatchError(block = {
            recommendationProductUseCase.useEmptyShopId = useEmptyShopId
            val item = recommendationProductUseCase.getData(recommendationProductUseCase.getRequestParams(pageNumber, shopId, pageName)).first()
            val list = recommendationProductUseCase.mapper.recommWidgetToListOfVisitables(item)
            if (list.isNullOrEmpty() && useEmptyShopId) {
                productLiveData.postValue(LiveDataResult.error(Exception("Getting empty personal recommendataion")))
            } else {
                productLiveData.postValue(LiveDataResult.success(list))
            }
        }, onError = {
            productLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun addToWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        if (model.isTopAds) {
            val params = RequestParams.create()
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
            topAdsWishlishedUseCase.execute(params, getSubscriber(callback))
        } else {
            addWishListUseCase.setParams(model.productId.toString(), userSession.userId)
            addWishListUseCase.execute(
                    onSuccess = {
                        callback.invoke(true, null)},
                    onError = {
                        callback.invoke(false, it)})
        }
    }

    fun getSubscriber(callback: ((Boolean, Throwable?) -> Unit)): Subscriber<WishlistModel> {
        return object : Subscriber<WishlistModel>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                callback.invoke(false, e)
            }

            override fun onNext(wishlistModel: WishlistModel) {
                if (wishlistModel.data != null) {
                    callback.invoke(true, null)
                }
            }
        }
    }


    fun removeFromWishlist(model: RecommendationItem, wishlistCallback: (((Boolean, Throwable?) -> Unit))) {
        removeWishListUseCase.setParams(model.productId.toString(), userSession.userId)
        removeWishListUseCase.execute(
                onSuccess = { wishlistCallback.invoke(true, null) },
                onError = { wishlistCallback.invoke(false, it) })
    }

    override fun onCleared() {
        super.onCleared()
        recommendationProductUseCase.unsubscribe()
    }
}