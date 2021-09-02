package com.tokopedia.home_recom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.home_recom.model.datamodel.RecomErrorResponse
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by yfsx on 30/08/21.
 */
class InfiniteRecomViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
        private val addWishListUseCase: Lazy<AddWishListUseCase>,
        private val removeWishListUseCase: Lazy<RemoveWishListUseCase>,
        private val topAdsWishlishedUseCase: Lazy<TopAdsWishlishedUseCase>,
        private val addToCartUseCase: Lazy<AddToCartUseCase>,
        private val miniCartListSimplifiedUseCase: Lazy<GetMiniCartListSimplifiedUseCase>,
        private val updateCartUseCase: Lazy<UpdateCartUseCase>,
        private val deleteCartUseCase: Lazy<DeleteCartUseCase>,
        private val dispatcher: RecommendationDispatcher
) : BaseViewModel(dispatcher.getMainDispatcher()) {

    val recommendationFirstLiveData: LiveData<List<RecommendationItemDataModel>> get() = _recommendationFirstLiveData
    private val _recommendationFirstLiveData = MutableLiveData<List<RecommendationItemDataModel>>()

    val recommendationNextLiveData: LiveData<List<RecommendationItemDataModel>> get() = _recommendationNextLiveData
    private val _recommendationNextLiveData = MutableLiveData<List<RecommendationItemDataModel>>()

    val recommendationWidgetData: LiveData<RecommendationWidget> get() = _recommendationWidgetData
    private val _recommendationWidgetData = MutableLiveData<RecommendationWidget>()

    val miniCartData: LiveData<MutableMap<String, MiniCartItem>> get() = _miniCartData
    private val _miniCartData = MutableLiveData<MutableMap<String, MiniCartItem>>()

    val errorGetRecomData: LiveData<RecomErrorResponse> get() = _errorGetRecomData
    private val _errorGetRecomData = MutableLiveData<RecomErrorResponse>()

    fun getRecommendationFirstPage(pageName: String, productId: String, queryParam: String) {
        launchCatchError(dispatcher.getIODispatcher(), {
            val result = getRecommendationUseCase.get().getData(getBasicRecomParams(pageName = pageName, productId = productId, queryParam = queryParam))
            if (result.isEmpty()) {
                _errorGetRecomData.postValue(RecomErrorResponse(isEmptyFirstPage = true))
            } else {
                _recommendationWidgetData.postValue(result[0])
                _recommendationFirstLiveData.postValue(mappingRecomDataModel(result))
            }
        }) {
            _errorGetRecomData.postValue(RecomErrorResponse(pageNumber = 1, errorThrowable = it, isErrorFirstPage = true))
        }
    }

    fun getRecommendationNextPage(pageName: String, productId: String, pageNumber: Int, queryParam: String) {
        launchCatchError(dispatcher.getIODispatcher(), {
            val result = getRecommendationUseCase.get().getData(getBasicRecomParams(pageName = pageName, pageNumber = pageNumber, productId = productId, queryParam = queryParam))
            if (result.isEmpty()) {
            } else {
                _recommendationNextLiveData.postValue(mappingRecomDataModel(result))
            }
        }) {
            _errorGetRecomData.postValue(RecomErrorResponse(pageNumber, it))
        }
    }

    private fun getBasicRecomParams(pageName: String = "", productId: String = "", pageNumber: Int = 1, queryParam: String = ""): GetRecommendationRequestParam {
        return GetRecommendationRequestParam(
                isTokonow = true,
                pageNumber = pageNumber,
                productIds = listOf(productId),
                pageName = "recom_1",
                xSource = "recom_widget",
                queryParam = queryParam)
    }

    private fun mappingRecomDataModel(recomData: List<RecommendationWidget>): List<RecommendationItemDataModel> {
        val recomItemList = mutableListOf<RecommendationItemDataModel>()
        if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
            val recomWidget = recomData.first().copy()
            if (recomWidget.isTokonow) {
                recomItemList.addAll(mappingMiniCartDataToRecommendation(recomWidget))
            } else {
                recomItemList.addAll(mappingDataRecomToModel(recomWidget))
            }
        }
        return recomItemList
    }

    private fun mappingDataRecomToModel(recomWidget: RecommendationWidget): List<RecommendationItemDataModel> {
        val recomItemList = mutableListOf<RecommendationItemDataModel>()
        recomWidget.recommendationItemList.forEach { item ->
            recomItemList.add(RecommendationItemDataModel(item))
        }
        return recomItemList
    }

    private fun mappingMiniCartDataToRecommendation(recomWidget: RecommendationWidget): List<RecommendationItemDataModel> {
        val recomItemList = mutableListOf<RecommendationItemDataModel>()
        recomWidget.recommendationItemList.forEach { item ->
            miniCartData.value?.let {
                if (item.isProductHasParentID()) {
                    var variantTotalItems = 0
                    it.values.forEach { miniCartItem ->
                        if (miniCartItem.productParentId == item.parentID.toString()) {
                            variantTotalItems += miniCartItem.quantity
                        }
                    }
                    item.updateItemCurrentStock(variantTotalItems)
                } else {
                    item.updateItemCurrentStock(it[item.productId.toString()]?.quantity
                            ?: 0)
                }
            }
            recomItemList.add(RecommendationItemDataModel(item))
        }
        return recomItemList
    }

    fun getMiniCart(shopId: String) {
        launchCatchError(dispatcher.getIODispatcher(), block = {
            miniCartListSimplifiedUseCase.get().setParams(listOf(shopId))
            val result = miniCartListSimplifiedUseCase.get().executeOnBackground()
            val data = result.miniCartItems.associateBy({ it.productId }) {
                it
            }
            _miniCartData.postValue(data.toMutableMap())
        }) {
        }
    }

}