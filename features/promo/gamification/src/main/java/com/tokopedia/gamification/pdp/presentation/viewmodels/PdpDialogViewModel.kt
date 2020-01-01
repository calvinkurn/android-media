package com.tokopedia.gamification.pdp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.GamingRecommendationParamResponse
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.data.di.modules.DispatcherModule
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationParamUseCase
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class PdpDialogViewModel @Inject constructor(val recommendationProductUseCase: GamingRecommendationProductUseCase,
                                             val paramUseCase: GamingRecommendationParamUseCase,
                                             val addWishListUseCase: AddWishListUseCase,
                                             val removeWishListUseCase: RemoveWishListUseCase,
                                             val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
                                             val userSession: UserSessionInterface,
                                             @Named(DispatcherModule.MAIN) val uiDispatcher: CoroutineDispatcher,
                                             @Named(DispatcherModule.IO) val workerDispatcher: CoroutineDispatcher) : BaseViewModel(uiDispatcher) {

    val recommendationLiveData: MutableLiveData<LiveDataResult<GamingRecommendationParamResponse>> = MutableLiveData()
    val productLiveData: MutableLiveData<LiveDataResult<List<Recommendation>>> = MutableLiveData()
    val titleLiveData: MutableLiveData<LiveDataResult<String>> = MutableLiveData()

    fun getRecommendationParams(pageName: String) {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = paramUseCase.getResponse(paramUseCase.getRequestParams(pageName))
                withContext(uiDispatcher){
                    recommendationLiveData.value = (LiveDataResult.success(response))
                    getProducts(0)
                }
            }
        }, onError = {
            it.printStackTrace()
            Log.wtf("NOOB", "1 Error - ${it}")
            recommendationLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getProducts(page: Int) {

        launchCatchError(block = {
            val params = recommendationLiveData.value!!.data!!.params
            withContext(workerDispatcher) {
                val item = recommendationProductUseCase.getData(recommendationProductUseCase.getRequestParams(params, page)).first()
                val list = recommendationProductUseCase.mapper.recommWidgetToListOfVisitables(item)

                productLiveData.postValue(LiveDataResult.success(list))
                if (titleLiveData.value == null) {
                    titleLiveData.postValue(LiveDataResult.success(item.title))
                }
            }
        }, onError = {
            it.printStackTrace()
            Log.wtf("NOOB", "2 Error - ${it}")
            productLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun addToWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        if (model.isTopAds) {
            val params = RequestParams.create()
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
            topAdsWishlishedUseCase.execute(params, object : Subscriber<WishlistModel>() {
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
            })
        } else {
            addWishListUseCase.createObservable(model.productId.toString(), userSession.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    callback.invoke(false, Throwable(errorMessage))
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    callback.invoke(true, null)
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                    // do nothing
                }

                override fun onSuccessRemoveWishlist(productId: String?) {
                    // do nothing
                }
            })
        }
    }


    fun removeFromWishlist(model: RecommendationItem, wishlistCallback: (((Boolean, Throwable?) -> Unit))) {
        removeWishListUseCase.createObservable(model.productId.toString(), userSession.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // do nothing
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // do nothing
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                wishlistCallback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                wishlistCallback.invoke(true, null)
            }
        })
    }


    override fun onCleared() {
        super.onCleared()
        recommendationProductUseCase.unsubscribe()
    }
}