package com.tokopedia.home_recom.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

/**
 * A Class ViewModel For Recommendation Page.
 *
 * @param graphqlRepository gql repository for getResponse from network with GQL request
 * @param userSessionInterface the handler of user session
 * @param getRecommendationUseCase use case for Recommendation Widget
 * @param addWishListUseCase use case for add wishlist
 * @param removeWishListUseCase use case for remove wishlist
 * @param topAdsWishlishedUseCase use case for add wishlist topads product item
 * @param dispatcher the dispatcher for coroutine
 * @param primaryProductQuery the raw query for get primary product
 */
open class RecommendationPageViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        @Named("Main") val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {
    /**
     * public variable
     */
    val recommendationListModel = MutableLiveData<List<RecommendationWidget>>()

    val xSource = "recom_landing_page"
    val pageName = "recom_1,recom_2,recom_3"

    /**
     * [getRecommendationList] is the void for get recommendation widgets from the network
     * @param productIds list of product Ids from deeplink
     * @param onErrorGetRecommendation the callback for handling error for ui
     */
    fun getRecommendationList(
            productIds: List<String>,
            queryParam: String,
            onErrorGetRecommendation: ((errorMessage: String?) -> Unit)?) {
        getRecommendationUseCase.execute(
                getRecommendationUseCase.getRecomParams(
                        pageNumber = 1,
                        productIds = productIds,
                        pageName = "recom_1,recom_2,recom_3",
                        queryParam = queryParam), object : Subscriber<List<RecommendationWidget>>() {
            override fun onNext(t: List<RecommendationWidget>?) {
                recommendationListModel.value = t
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                onErrorGetRecommendation?.invoke(e?.message)
            }
        }
        )
    }

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
        removeWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener{
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

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

}