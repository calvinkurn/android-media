package com.tokopedia.home_recom.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_recom.PARAM_PRODUCT_ID
import com.tokopedia.home_recom.PARAM_X_SOURCE
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

open class RecommendationPageViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        @Named("Main") val dispatcher: CoroutineDispatcher,
        @Named("primaryQuery") private val primaryProductQuery: String
) : BaseViewModel(dispatcher) {
    val recommendationListModel = MutableLiveData<List<RecommendationWidget>>()
    val productInfoDataModel = MutableLiveData<ProductInfoDataModel>()

    val xSource = "recom_landing_page"
    val pageName = "recom_1,recom_2,recom_3"

    fun getPrimaryProduct(productId: String) {
        launchCatchError(block = {
            val gqlData = withContext(Dispatchers.IO) {
                val cacheStrategy =
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

                val params = mapOf(
                        PARAM_PRODUCT_ID to productId.toInt(),
                        PARAM_X_SOURCE to xSource
                )

                val gqlRecommendationRequest = GraphqlRequest(
                        primaryProductQuery,
                        PrimaryProductEntity::class.java,
                        params
                )

                graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
            }
            gqlData.getSuccessData<PrimaryProductEntity>().productRecommendationProductDetail?.let {
                val productDetailResponse = it.data.get(0).recommendation.get(0)
                productInfoDataModel.value = ProductInfoDataModel(productDetailResponse)
            }
        }) {
        }
    }

    fun getRecommendationList(
            productIds: List<String>,
            onErrorGetRecommendation: ((errorMessage: String?) -> Unit)?) {
        getRecommendationUseCase.execute(
                getRecommendationUseCase.getRecomParams(
                        1,
                        xSource,
                        pageName,
                        productIds), object : Subscriber<List<RecommendationWidget>>() {
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

    fun isLoggedIn() = userSessionInterface.isLoggedIn


}