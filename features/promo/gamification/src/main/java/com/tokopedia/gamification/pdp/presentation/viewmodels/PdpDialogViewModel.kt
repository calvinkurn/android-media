package com.tokopedia.gamification.pdp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.data.di.modules.DispatcherModule
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class PdpDialogViewModel @Inject constructor(private val recommendationProductUseCase: GamingRecommendationProductUseCase,
                                             private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
                                             private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
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

    fun addToWishlistV2(
        model: RecommendationItem,
        actionListener: WishlistV2ActionListener
    ) {
        addToWishlistV2UseCase.setParams(model.productId.toString(), userSession.userId)
        addToWishlistV2UseCase.execute(
            onSuccess = { result ->
                if (result is Success) actionListener.onSuccessAddWishlist(result.data, model.productId.toString())},
            onError = {
                actionListener.onErrorAddWishList(it, model.productId.toString())})
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

    fun removeFromWishlistV2(model: RecommendationItem, actionListener: WishlistV2ActionListener) {
        deleteWishlistV2UseCase.setParams(model.productId.toString(), userSession.userId)
        deleteWishlistV2UseCase.execute(
            onSuccess = { result ->
                if (result is Success) {
                    actionListener.onSuccessRemoveWishlist(result.data, model.productId.toString())
                } },
            onError = { actionListener.onErrorRemoveWishlist(it, model.productId.toString()) })
    }

    override fun onCleared() {
        super.onCleared()
        recommendationProductUseCase.unsubscribe()
        addToWishlistV2UseCase.cancelJobs()
        deleteWishlistV2UseCase.cancelJobs()
    }
}
