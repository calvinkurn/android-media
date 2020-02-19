package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class AddToCartDoneViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        private val rawQueries: Map<String, String>,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        @Named("Main")
        val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher
) {
    val recommendationProduct = MutableLiveData<Result<List<RecommendationWidget>>>()

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
        launchCatchError(block = {
            val recommendationWidget = withContext(Dispatchers.IO) {
                if (GlobalConfig.isCustomerApp())
                    loadRecommendationProduct(productId)
                else listOf()
            }
            recommendationProduct.value = Success(recommendationWidget)

        }) {
            recommendationProduct.value = Fail(it)
        }
    }

    private fun loadRecommendationProduct(productId: String): List<RecommendationWidget> {
        try {
            val data = getRecommendationUseCase.createObservable(getRecommendationUseCase.getRecomParams(
                    pageNumber = TopAdsDisplay.DEFAULT_PAGE_NUMBER,
                    pageName = TopAdsDisplay.DEFAULT_PAGE_NAME,
                    productIds = arrayListOf(productId)
            )).toBlocking()
            return data.first()?: emptyList()
        } catch (e: Throwable) {
            Timber.d(e)
            throw e
        }
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