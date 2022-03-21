package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationItemDataModel
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AddToCartDoneViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddToWishlistV2UseCase,
        private val removeWishlistUseCase: DeleteWishlistV2UseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        val dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.main) {
    val recommendationProduct = MutableLiveData<Result<List<RecommendationWidget>>>()
    private val _addToCartLiveData = MutableLiveData<Result<AddToCartDataModel>>()
    val addToCartLiveData: LiveData<Result<AddToCartDataModel>>
        get() = _addToCartLiveData

    companion object {
        object TopAdsDisplay {
            const val DEFAULT_PAGE_NUMBER = 1
            const val DEFAULT_PAGE_NAME = "pdp_atc_1,pdp_atc_2"
        }
    }

    fun getRecommendationProduct(productId: String) {
        launchCatchError(block = {
            val recommendationWidget = withContext(dispatcher.io) {
                if (!GlobalConfig.isSellerApp())
                    loadRecommendationProduct(productId)
                else listOf()
            }
            recommendationProduct.value = Success(recommendationWidget)

        }) {
            recommendationProduct.value = Fail(it)
        }
    }

    private suspend fun loadRecommendationProduct(productId: String): List<RecommendationWidget> {
        try {
            val requestParams = GetRecommendationRequestParam(
                    pageNumber = TopAdsDisplay.DEFAULT_PAGE_NUMBER,
                    pageName = TopAdsDisplay.DEFAULT_PAGE_NAME,
                    productIds = arrayListOf(productId)
            )
            return getRecommendationUseCase.getData(requestParams)
        } catch (e: Throwable) {
            Timber.d(e)
            throw e
        }
    }

    fun addWishList(productId: String, callback: (Boolean, Throwable?) -> Unit) {
        addWishListUseCase.setParams(productId, userSessionInterface.userId)
        addWishListUseCase.execute(onSuccess = { callback.invoke(true, null) },
                onError = { callback.invoke(false, it) })
    }

    fun removeWishList(productId: String, callback: (Boolean, Throwable?) -> Unit) {
        removeWishlistUseCase.setParams(productId, userSessionInterface.userId)
        removeWishlistUseCase.execute(onSuccess = { callback.invoke(true, null) },
                onError = { callback.invoke(false, it) })
    }

    fun isLoggedIn(): Boolean = userSessionInterface.isLoggedIn

    fun addToCart(dataModel: AddToCartDoneRecommendationItemDataModel) {
        launchCatchError(dispatcher.io, block = {
            val recommendationItem = dataModel.recommendationItem
            val requestParams = RequestParams.create()
            val addToCartRequestParams = AddToCartRequestParams().apply {
                productId = recommendationItem.productId
                shopId = recommendationItem.shopId
                quantity = if (recommendationItem.minOrder > 0) recommendationItem.minOrder else 1
                notes = ""
                userId = userSessionInterface.userId
                productName = recommendationItem.name
                category = recommendationItem.categoryBreadcrumbs
                price = recommendationItem.price
            }
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
            val result = addToCartUseCase.createObservable(requestParams).toBlocking().single()
            if (result.isStatusError()) {
                _addToCartLiveData.postValue(MessageErrorException(result.getAtcErrorMessage()
                        ?: "").asFail())
            } else {
                dataModel.isAddedToCart = true
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }) {
            _addToCartLiveData.postValue(it.asFail())
        }
    }
}