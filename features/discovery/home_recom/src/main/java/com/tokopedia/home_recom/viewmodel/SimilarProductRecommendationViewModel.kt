package com.tokopedia.home_recom.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home_recom.util.Response
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
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
 * Created by Lukas on 26/08/19
 */
@SuppressLint("SyntheticAccessor")
open class SimilarProductRecommendationViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
        dispatcher: RecommendationDispatcher
) : BaseViewModel(dispatcher.getMainDispatcher()){

    private val _recommendationItem = MutableLiveData<Response<List<RecommendationItem>>>()
    val recommendationItem: LiveData<Response<List<RecommendationItem>>> get() = _recommendationItem


    fun getSimilarProductRecommendation(page: Int = 1, queryParam: String, productId: String){
        if(page == 1 && _recommendationItem.value != null) _recommendationItem.value = null
        if (_recommendationItem.value == null) _recommendationItem.postValue(Response.loading())
        else _recommendationItem.postValue(Response.loadingMore(_recommendationItem.value?.data))
        val params = singleRecommendationUseCase.getRecomParams(pageNumber = page, productIds = listOf(productId), queryParam = queryParam)
        singleRecommendationUseCase.execute(params,object: Subscriber<List<RecommendationItem>>(){
            override fun onNext(list: List<RecommendationItem>) {
                _recommendationItem.postValue(Response.success(combineList(_recommendationItem.value?.data
                        ?: emptyList(), list)))
            }

            override fun onCompleted() {}

            override fun onError(throwable: Throwable) {
                _recommendationItem.postValue(Response.error(throwable.localizedMessage, _recommendationItem.value?.data))
            }

        })
    }

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

    /**
     * [addWishlist] is the void for handling adding wishlist item
     * @param model the recommendation item product is clicked
     * @param callback the callback for handling [added or removed, throwable] to UI
     */
    fun addWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)){
        if(model.isTopAds){
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
            addWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener {
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

    /**
     * [addWishlist] is the void for handling removing wishlist item
     * @param model the recommendation item product is clicked
     * @param wishlistCallback the callback for handling [added or removed, throwable] to UI
     */
    fun removeWishlist(model: RecommendationItem, wishlistCallback: (((Boolean, Throwable?) -> Unit))){
        removeWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener {
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

    internal fun <T> combineList(first: List<T>, second: List<T>): List<T>{
        return ArrayList(first).apply { addAll(second) }
    }
}