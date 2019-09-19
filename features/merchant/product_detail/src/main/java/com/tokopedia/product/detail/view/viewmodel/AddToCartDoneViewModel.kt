package com.tokopedia.product.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.data.util.getSuccessData
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class AddToCartDoneViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        private val rawQueries: Map<String, String>,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        @Named("Main")
        val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher
) {
    val recommendationProduct = MutableLiveData<RequestDataState<List<RecommendationWidget>>>()

    companion object {
        object TopAdsDisplay {
            const val KEY_USER_ID = "userID"
            const val KEY_PAGE_NAME = "pageName"
            const val KEY_XDEVICE = "xDevice"
            const val DEFAULT_DEVICE = "android"
            const val DEFAULT_SRC_PAGE = "pdp_after_atc"
            const val KEY_PRODUCT_ID = "productIDs"
            const val KEY_XSOURCE = "xSource"
            const val KEY_PAGE_NUMBER = "pageNumber"
            const val DEFAULT_PAGE_NUMBER = 1
            const val DEFAULT_PAGE_NAME = "pdp_atc_1,pdp_atc_2"
        }
    }

    fun getRecommendationProduct(productId: String) {
        launch {
            val topAdsProductDef = if (GlobalConfig.isCustomerApp() &&
                    (recommendationProduct.value as? Loaded)?.data as? Success == null) {
                recommendationProduct.value = Loading
                loadRecommendationProduct(productId)
            } else null

            topAdsProductDef?.await()?.let {
                val recommendationWidget = RecommendationEntityMapper.mappingToRecommendationModel((it.data as? Success)?.data
                        ?: return@launch)
                recommendationProduct.value = Loaded(Success(recommendationWidget))
            }
        }
    }

    private fun loadRecommendationProduct(productId: String) = async(Dispatchers.IO) {
        val topadsParams = generateRecommendationProductParams(productId)
        val topAdsRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_RECOMMEN_PRODUCT],
                RecomendationEntity::class.java, topadsParams)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        try {
            Loaded(Success(graphqlRepository.getReseponse(listOf(topAdsRequest), cacheStrategy)
                    .getSuccessData<RecomendationEntity>().productRecommendationWidget?.data
                    ?: emptyList()))
        } catch (t: Throwable) {
            Loaded(Fail(t))
        }
    }

    private fun generateRecommendationProductParams(productId: String): Map<String, Any> {
        return mapOf(
                TopAdsDisplay.KEY_USER_ID to (userSessionInterface.userId.toIntOrNull() ?: 0),
                TopAdsDisplay.KEY_PAGE_NAME to TopAdsDisplay.DEFAULT_PAGE_NAME,
                TopAdsDisplay.KEY_PAGE_NUMBER to TopAdsDisplay.DEFAULT_PAGE_NUMBER,
                TopAdsDisplay.KEY_XDEVICE to TopAdsDisplay.DEFAULT_DEVICE,
                TopAdsDisplay.KEY_XSOURCE to TopAdsDisplay.DEFAULT_SRC_PAGE,
                TopAdsDisplay.KEY_PRODUCT_ID to productId
        )
    }

    fun addWishList(
            productId: String,
            callback: (Boolean, Throwable?) -> Unit
    ) {
        addWishListUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                callback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessAddWishlist(productId: String?) {
                callback.invoke(true, null)
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                // no op
            }
        })
    }

    fun removeWishList(
            productId: String,
            callback: (Boolean, Throwable?) -> Unit
    ) {
        removeWishlistUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // no op
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                callback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                callback.invoke(true, null)
            }
        })
    }

    fun isLoggedIn(): Boolean = userSessionInterface.isLoggedIn


}