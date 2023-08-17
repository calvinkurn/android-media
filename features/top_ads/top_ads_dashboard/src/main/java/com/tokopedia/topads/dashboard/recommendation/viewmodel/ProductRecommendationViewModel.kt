package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.usecase.TopadsGetProductRecommendationV2Usecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName.TopAdsGroupValidateNameV2
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.INSIGHT_CENTRE_GQL_SOURCE
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductListUiModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ProductRecommendationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val bidInfoUseCase: BidInfoUseCase,
    private val userSession: UserSessionInterface,
    private val topadsGetProductRecommendationV2Usecase: TopadsGetProductRecommendationV2Usecase,
    private val mapper: ProductRecommendationMapper,
    private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase,
) : BaseViewModel(dispatcher.main), CoroutineScope {

    private val _productItemsLiveData =
        MutableLiveData<TopadsProductListState<List<ProductListUiModel>>>()
    val productItemsLiveData: LiveData<TopadsProductListState<List<ProductListUiModel>>>
        get() = _productItemsLiveData

    private val _validateNameLiveData = MutableLiveData<TopAdsGroupValidateNameV2>()
    val validateNameLiveData: LiveData<TopAdsGroupValidateNameV2>
        get() = _validateNameLiveData

    private val _bidInfoLiveData = MutableLiveData<List<TopadsBidInfo.DataItem>>()
    val bidInfoLiveData: LiveData<List<TopadsBidInfo.DataItem>>
        get() = _bidInfoLiveData

    fun loadProductList() {
        launchCatchError(dispatcher.main, {
            val data = topadsGetProductRecommendationV2Usecase(userSession.shopId)
            if (data.topadsGetProductRecommendation.errors.isNullOrEmpty() && data.topadsGetProductRecommendation.data.products.isNotEmpty()) {
                _productItemsLiveData.value =
                    TopadsProductListState.Success(mapper.convertToProductItemUiModel(data.topadsGetProductRecommendation.data.products))
            } else {
                _productItemsLiveData.value = TopadsProductListState.Fail(Exception())
            }
        }, {
            _productItemsLiveData.value = TopadsProductListState.Fail(it)
        })
    }

    fun validateGroup(
        groupName: String,
    ) {
        topAdsGroupValidateNameUseCase.setParams(groupName, INSIGHT_CENTRE_GQL_SOURCE)
        topAdsGroupValidateNameUseCase.execute({
            _validateNameLiveData.value = it.topAdsGroupValidateName
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    fun getBidInfo(
        suggestions: List<DataSuggestions>,
    ) {
        bidInfoUseCase.setParams(suggestions, ParamObject.AUTO_BID_STATE, /*INSIGHT_CENTRE_GQL_SOURCE*/ "android.group_detail")
        bidInfoUseCase.executeQuerySafeMode(
            {
                _bidInfoLiveData.value = it.topadsBidInfo.data
            },
            { throwable ->
                throwable.printStackTrace()
            }
        )
    }
}
