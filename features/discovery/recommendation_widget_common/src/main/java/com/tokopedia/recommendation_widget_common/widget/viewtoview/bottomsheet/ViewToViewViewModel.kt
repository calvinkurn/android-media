package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import timber.log.Timber
import javax.inject.Inject

class ViewToViewViewModel @Inject constructor(
    private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
    private val addToCartUseCase: Lazy<AddToCartUseCase>,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {
    val viewToViewRecommendationLiveData: LiveData<Result<List<ViewToViewDataModel>>>
        get() = _viewToViewRecommendationLiveData
    private val _viewToViewRecommendationLiveData = MutableLiveData<Result<List<ViewToViewDataModel>>>()

    val viewToViewATCStatusLiveData: LiveData<ViewToViewATCStatus>
        get() = _viewToViewATCStatusLiveData
    private val _viewToViewATCStatusLiveData = MutableLiveData<ViewToViewATCStatus>()

    fun getUserId() : String {
        return if(userSession.isLoggedIn) userSession.userId else "0"
    }

    val isUserSessionActive: Boolean
        get() = userSession.isLoggedIn

    fun getViewToViewProductRecommendation(
        queryParams: String,
        hasAtcButton: Boolean,
        districtId: String,
    ) {
        launchCatchError(block = {
            val requestQueryParams = "$queryParams&user_districtId=$districtId"
            val requestParam = GetRecommendationRequestParam(
                queryParam = requestQueryParams,
            )
            val loadingList = List(PRODUCT_COUNT) { ViewToViewDataModel.Loading(hasAtcButton) }
            _viewToViewRecommendationLiveData.postValue(Success(loadingList))
            val recommendation = getRecommendationUseCase.get().getData(requestParam)
            val result = if(recommendation.isNotEmpty()
                && recommendation.first().recommendationItemList.isNotEmpty()) {
                Success(
                    recommendation.first().recommendationItemList
                        .map { it.toViewToViewDataModelProduct(hasAtcButton) }
                )
            } else Fail(Exception("Empty Recommendation"))
            _viewToViewRecommendationLiveData.postValue(result)
        }) {
            _viewToViewRecommendationLiveData.postValue(Fail(it))
            Timber.e(it)
        }
    }

    fun retryViewToViewProductRecommendation(
        queryParams: String,
        hasAtcButton: Boolean,
        districtId: String,
    ) {
        getViewToViewProductRecommendation(queryParams, hasAtcButton, districtId)
    }

    private fun RecommendationItem.toViewToViewDataModelProduct(
        hasAtcButton: Boolean,
    ): ViewToViewDataModel.Product {
        return ViewToViewDataModel.Product(
            productId.toString(),
            shopId.toString(),
            name,
            minOrder,
            price,
            toProductCardModel(hasAddToCartButton = hasAtcButton),
            this,
        )
    }

    private fun ViewToViewDataModel.Product.createAddToCartRequestParams(): AddToCartRequestParams {
        return AddToCartRequestParams(
            productId = id.toLongOrZero(),
            shopId = shopId.toIntOrZero(),
            quantity = minOrder,
            productName = productName,
            price = price,
            userId = if (userSession.isLoggedIn) userSession.userId else "0"
        )
    }

    fun addToCart(
        product: ViewToViewDataModel.Product,
    ) {
        launchCatchError(
            block = {
                val useCase = addToCartUseCase.get()
                useCase.setParams(product.createAddToCartRequestParams())
                useCase?.execute(
                    onSuccess = { handleATCSuccess(it, product) },
                    onError = ::handleATCError,
                )
            }, onError = ::handleATCError
        )
    }

    private fun handleATCSuccess(
        atcData: AddToCartDataModel,
        product: ViewToViewDataModel.Product,
    ) {
        val atcStatus = if (atcData.isStatusError()) {
            ViewToViewATCStatus.Failure(atcData.getAtcErrorMessage() ?: "")
        } else {
            ViewToViewATCStatus.Success(atcData.data.message.firstOrNull() ?: "", product)
        }

        _viewToViewATCStatusLiveData.postValue(atcStatus)
    }

    private fun handleATCError(e: Throwable) {
        Timber.e(e)
        _viewToViewATCStatusLiveData.postValue(
            ViewToViewATCStatus.Failure(e.message ?: "")
        )
    }

    companion object {
        private const val PRODUCT_COUNT = 4
    }
}
