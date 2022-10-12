package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlistcommon.data.WishlistV2Params
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Utils
import com.tokopedia.wishlist.util.WishlistV2Utils.convertWishlistV2IntoWishlistUiModel
import com.tokopedia.wishlist.util.WishlistV2Utils.organizeWishlistV2Data
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class WishlistV2ViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val wishlistV2UseCase: WishlistV2UseCase,
    private val deleteWishlistV2UseCase: com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase,
    private val bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase,
    private val countDeletionWishlistV2UseCase: DeleteWishlistProgressUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
    private val atcUseCase: AddToCartUseCase
) : BaseViewModel(dispatcher.main) {
    private var recommSrc = ""

    private val _wishlistV2 = MutableLiveData<Result<WishlistV2Response.Data.WishlistV2>>()
    val wishlistV2: LiveData<Result<WishlistV2Response.Data.WishlistV2>>
        get() = _wishlistV2

    private val _wishlistV2Data = MutableLiveData<Result<List<WishlistV2TypeLayoutData>>>()
    val wishlistV2Data: LiveData<Result<List<WishlistV2TypeLayoutData>>>
        get() = _wishlistV2Data

    private val _deleteWishlistV2Result =
        MutableLiveData<Result<com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response.Data.WishlistRemoveV2>>()
    val deleteWishlistV2Result: LiveData<Result<com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _deleteWishlistV2Result

    private val _bulkDeleteWishlistV2Result =
        MutableLiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>()
    val bulkDeleteWishlistV2Result: LiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>
        get() = _bulkDeleteWishlistV2Result

    private val _atcResult = MutableLiveData<Result<AddToCartDataModel>>()
    val atcResult: LiveData<Result<AddToCartDataModel>>
        get() = _atcResult

    private val _deleteWishlistProgressResult =
        MutableLiveData<Result<DeleteWishlistProgressResponse.DeleteWishlistProgress>>()
    val deleteWishlistProgressResult: LiveData<Result<DeleteWishlistProgressResponse.DeleteWishlistProgress>>
        get() = _deleteWishlistProgressResult

    fun loadWishlistV2(params: WishlistV2Params, typeLayout: String?, isAutomaticDelete: Boolean, isUsingCollection: Boolean) {
        launch {
            try {
                val wishlistV2Response = wishlistV2UseCase.executeSuspend(params).wishlistV2
                recommSrc = if (wishlistV2Response.totalData == 0) EMPTY_WISHLIST_PAGE_NAME else WISHLIST_PAGE_NAME
                _wishlistV2.value = Success(wishlistV2Response)
                _wishlistV2Data.value = Success(organizeWishlistV2Data(
                    convertWishlistV2IntoWishlistUiModel(wishlistV2Response),
                    typeLayout, isAutomaticDelete, getRecommendationWishlistV2(1, listOf(), recommSrc), getTopAdsData(), isUsingCollection))
            } catch (e: Exception) {
                _wishlistV2.value = Fail(e)
                _wishlistV2Data.value = Fail(e)
            }
        }
    }

    fun loadRecommendation(page: Int) {
        val listData = arrayListOf<WishlistV2TypeLayoutData>()
        launch {
            try {
                val recommItems =
                    getRecommendationWishlistV2(page, listOf(), recommSrc)
                recommItems.recommendationProductCardModelData.forEach { item ->
                    listData.add(WishlistV2TypeLayoutData(item, TYPE_RECOMMENDATION_LIST))
                }
                _wishlistV2Data.value = Success(listData)
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    fun deleteWishlistV2(productId: String, userId: String) {
        launch {
            deleteWishlistV2UseCase.setParams(productId, userId)
            _deleteWishlistV2Result.value =
                deleteWishlistV2UseCase.executeOnBackground()
        }
    }

    fun bulkDeleteWishlistV2(
        listProductId: List<String>,
        userId: String,
        mode: Int,
        additionalParams: WishlistV2BulkRemoveAdditionalParams
    ) {
        launch {
            _bulkDeleteWishlistV2Result.value = bulkDeleteWishlistV2UseCase.executeSuspend(
                listProductId,
                userId,
                mode,
                additionalParams
            , "")
        }
    }

    fun getDeleteWishlistProgress() {
        launchCatchError(block = {
            val result = countDeletionWishlistV2UseCase(Unit)
            if (result.deleteWishlistProgress.status == WishlistV2CommonConsts.OK && result.deleteWishlistProgress.errorMessage.isEmpty()) {
                _deleteWishlistProgressResult.postValue(Success(result.deleteWishlistProgress))
            } else {
                _deleteWishlistProgressResult.postValue(Fail(Throwable()))
            }
        }, onError = {
            _deleteWishlistProgressResult.postValue(Fail(it))
        })
    }

    fun doAtc(atcParams: AddToCartRequestParams) {
        launch {
            try {
                atcUseCase.setParams(atcParams)
                val result = atcUseCase.executeOnBackground()
                _atcResult.value = Success(result)
            } catch (e: Exception) {
                Timber.d(e)
                _atcResult.value = Fail(e)
            }
        }
    }

    suspend fun getRecommendationWishlistV2(page: Int, productIds: List<String>, pageName: String): WishlistV2RecommendationDataModel {
        val recommendation = singleRecommendationUseCase.getData(
            GetRecommendationRequestParam(
                        pageNumber = page,
                        productIds = productIds,
                        pageName = pageName)
        )
        return WishlistV2RecommendationDataModel(WishlistV2Utils.convertRecommendationIntoProductDataModel(recommendation.recommendationItemList),
            recommendation.recommendationItemList, recommendation.title
        )
    }

    suspend fun getTopAdsData(): TopAdsImageViewModel? {
        return try {
            val queryParams =
                topAdsImageViewUseCase.getQueryMap(
                    "",
                    WISHLIST_TOPADS_SOURCE,
                    "",
                    WISHLIST_TOPADS_ADS_COUNT,
                    WISHLIST_TOPADS_DIMENS,
                    ""
                )
            topAdsImageViewUseCase.getImageData(queryParams).firstOrNull()
        } catch (t: Throwable) {
            null
        }
    }

    companion object {
        private var recommPosition = 4
        private var recommPositionDefault = 4
        private var recommWithTickerPosition = 5
        private const val WISHLIST_TOPADS_SOURCE = "6"
        private const val WISHLIST_TOPADS_ADS_COUNT = 1
        private const val WISHLIST_TOPADS_DIMENS = 3
        private const val WISHLIST_PAGE_NAME = "wishlist"
        private const val EMPTY_WISHLIST_PAGE_NAME = "empty_wishlist"
    }
}
