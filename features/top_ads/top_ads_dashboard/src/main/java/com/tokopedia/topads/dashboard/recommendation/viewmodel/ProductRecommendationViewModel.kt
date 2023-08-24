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
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.INSIGHT_CENTRE_CREATE_GROUP_SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.INSIGHT_CENTRE_BID_INFO_SOURCE
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductListUiModel
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetGroupDetailListUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ProductRecommendationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val bidInfoUseCase: BidInfoUseCase,
    private val userSession: UserSessionInterface,
    private val topadsGetProductRecommendationV2Usecase: TopadsGetProductRecommendationV2Usecase,
    private val mapper: ProductRecommendationMapper,
    private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
    private val topAdsGetGroupDetailListUseCase: TopAdsGetGroupDetailListUseCase,
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

    private val _createGroupLiveData = MutableLiveData<TopadsProductListState<String>>()
    val createGroupLiveData: LiveData<TopadsProductListState<String>>
        get() = _createGroupLiveData

    private val _groupListLiveData = MutableLiveData<List<GroupItemUiModel>>()
    val groupListLiveData : LiveData<List<GroupItemUiModel>>
    get() = _groupListLiveData

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
        topAdsGroupValidateNameUseCase.setParams(groupName, INSIGHT_CENTRE_BID_INFO_SOURCE)
        topAdsGroupValidateNameUseCase.execute({
            _validateNameLiveData.value = it.topAdsGroupValidateName
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    fun getBidInfo(
        suggestions: List<DataSuggestions>,
    ) {
        bidInfoUseCase.setParams(
            suggestions,
            ParamObject.AUTO_BID_STATE,
            INSIGHT_CENTRE_BID_INFO_SOURCE
        )
        bidInfoUseCase.executeQuerySafeMode(
            {
                _bidInfoLiveData.value = it.topadsBidInfo.data
            },
            { throwable ->
                throwable.printStackTrace()
            }
        )
    }

    fun getTopadsGroupList(search: String) {
        launchCatchError(block = {
            val response = topAdsGetGroupDetailListUseCase.executeOnBackground(search)
            _groupListLiveData.value = response
        }, onError = {
            _groupListLiveData.value = listOf()
        })
    }

    fun topAdsCreateGroup(
        productIds: List<String>,
        currentGroupName: String,
        dailyBudget: Double,
    ) {
        val param =
            topAdsCreateUseCase.createRequestParamActionCreate(
                productIds,
                currentGroupName,
                0.0,
                0.0,
                dailyBudget,
                INSIGHT_CENTRE_CREATE_GROUP_SOURCE
            )
        launchCatchError(block = {
            val response = topAdsCreateUseCase.execute(param)

            val dataGroup = response.topadsManageGroupAds.groupResponse
            val dataKeyword = response.topadsManageGroupAds.keywordResponse
            if (dataGroup.errors.isNullOrEmpty() && dataKeyword.errors.isNullOrEmpty()) {
                _createGroupLiveData.value = TopadsProductListState.Success(dataGroup.data.id)
            } else {
                _createGroupLiveData.value = TopadsProductListState.Fail(Exception())
            }
        }, onError = {
            _createGroupLiveData.value = TopadsProductListState.Fail(it)
        })
    }

    fun getSelectedProductItems(): List<ProductListUiModel>? {
        return when (val products = productItemsLiveData.value) {
            is TopadsProductListState.Success -> {
                products.data.filter {
                    (it as? ProductItemUiModel)?.isSelected ?: false
                }
            }
            else -> {
                null
            }
        }
    }
}
