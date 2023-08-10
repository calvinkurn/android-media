package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.usecase.TopadsGetProductRecommendationV2Usecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ProductRecommendationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val topadsGetProductRecommendationV2Usecase: TopadsGetProductRecommendationV2Usecase,
    private val mapper: ProductRecommendationMapper,
) : BaseViewModel(dispatcher.main), CoroutineScope {

    private val _productItemsLiveData =
        MutableLiveData<TopadsProductListState<List<ProductItemUiModel>>>()
    val productItemsLiveData: LiveData<TopadsProductListState<List<ProductItemUiModel>>>
        get() = _productItemsLiveData

    fun getProductList(){
        launchCatchError(dispatcher.main, {
            val data = topadsGetProductRecommendationV2Usecase(userSession.shopId)
            if (data.topadsGetProductRecommendation.errors.isNullOrEmpty() && data.topadsGetProductRecommendation.data.products.isNotEmpty()){
                _productItemsLiveData.value = TopadsProductListState.Success(mapper.convertToProductItemUiModel(data.topadsGetProductRecommendation.data.products))
            } else {
                _productItemsLiveData.value = TopadsProductListState.Fail(Exception())
            }
        }, {
            _productItemsLiveData.value = TopadsProductListState.Fail(it)
        })
    }
}
