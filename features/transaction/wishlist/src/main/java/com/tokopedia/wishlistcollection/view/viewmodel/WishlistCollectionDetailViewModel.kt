package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.util.WishlistV2Consts.EMPTY_WISHLIST_PAGE_NAME
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_TOPADS_ADS_COUNT
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_TOPADS_DIMENS
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_TOPADS_SOURCE
import com.tokopedia.wishlist.util.WishlistV2Utils.convertCollectionItemsIntoWishlistUiModel
import com.tokopedia.wishlist.util.WishlistV2Utils.convertRecommendationIntoProductDataModel
import com.tokopedia.wishlist.util.WishlistV2Utils.organizeWishlistV2Data
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionItemsParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionItemsUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WishlistCollectionDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionItemsUseCase: GetWishlistCollectionItemsUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val singleRecommendationUseCase: GetSingleRecommendationUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionItems =
        MutableLiveData<Result<GetWishlistCollectionItemsResponse.Data.GetWishlistCollectionItems>>()
    val collectionItems: LiveData<Result<GetWishlistCollectionItemsResponse.Data.GetWishlistCollectionItems>>
        get() = _collectionItems

    private val _collectionData = MutableLiveData<Result<List<WishlistV2TypeLayoutData>>>()
    val collectionData: LiveData<Result<List<WishlistV2TypeLayoutData>>>
        get() = _collectionData

    fun getWishlistCollectionItems(
        params: GetWishlistCollectionItemsParams,
        typeLayout: String?,
        isAutomaticDelete: Boolean
    ) {
        launch(dispatcher.main) {
            val result =
                withContext(dispatcher.io) {
                    getWishlistCollectionItemsUseCase.setParams(params)
                    getWishlistCollectionItemsUseCase.executeOnBackground()
                }
            if (result is Success) {
                _collectionItems.value = result
                _collectionData.value = Success(
                    organizeWishlistV2Data(
                        convertCollectionItemsIntoWishlistUiModel(result.data),
                        typeLayout,
                        isAutomaticDelete,
                        getRecommendationWishlistV2(
                            1, listOf(),
                            EMPTY_WISHLIST_PAGE_NAME
                        ),
                        getTopAdsData()
                    )
                )
            } else {
                val error = (result as Fail).throwable
                _collectionItems.value = Fail(error)
            }
        }
    }

    fun loadRecommendation(page: Int) {
        val listData = arrayListOf<WishlistV2TypeLayoutData>()
        launch {
            try {
                val recommItems = getRecommendationWishlistV2(
                    page, listOf(),
                    EMPTY_WISHLIST_PAGE_NAME
                )
                recommItems.recommendationProductCardModelData.forEach { item ->
                    listData.add(
                        WishlistV2TypeLayoutData(
                            item,
                            WishlistV2Consts.TYPE_RECOMMENDATION_LIST
                        )
                    )
                }
                _collectionData.value = Success(listData)
            } catch (e: Exception) {
                _collectionData.value = Fail(e)
            }
        }
    }

    suspend fun getRecommendationWishlistV2(
        page: Int,
        productIds: List<String>,
        pageName: String
    ): WishlistV2RecommendationDataModel {
        val recommendation = singleRecommendationUseCase.getData(
            GetRecommendationRequestParam(
                pageNumber = page,
                productIds = productIds,
                pageName = pageName
            )
        )
        return WishlistV2RecommendationDataModel(
            convertRecommendationIntoProductDataModel(recommendation.recommendationItemList),
            recommendation.recommendationItemList, recommendation.title
        )
    }

    suspend fun getTopAdsData(): TopAdsImageViewModel?  {
        return topAdsImageViewUseCase.getImageData(topAdsImageViewUseCase.getQueryMap("",
            WISHLIST_TOPADS_SOURCE, "",
            WISHLIST_TOPADS_ADS_COUNT,
            WISHLIST_TOPADS_DIMENS, "")).firstOrNull()
    }
}