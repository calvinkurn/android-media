package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.WishlistV2BulkRemoveAdditionalParams
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlist.util.WishlistIdlingResource
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_TOPADS_ADS_COUNT
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_TOPADS_DIMENS
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_TOPADS_SOURCE
import com.tokopedia.wishlist.util.WishlistV2Utils.convertCollectionItemsIntoWishlistUiModel
import com.tokopedia.wishlist.util.WishlistV2Utils.convertRecommendationIntoProductDataModel
import com.tokopedia.wishlist.util.WishlistV2Utils.organizeWishlistV2Data
import com.tokopedia.wishlistcollection.data.params.*
import com.tokopedia.wishlistcollection.data.response.*
import com.tokopedia.wishlistcollection.domain.*
import com.tokopedia.wishlistcommon.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcommon.data.response.UpdateWishlistCollectionResponse
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.UpdateWishlistCollectionUseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class WishlistCollectionDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionItemsUseCase: GetWishlistCollectionItemsUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
    private val deleteWishlistV2UseCase: com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase,
    private val bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase,
    private val deleteCollectionItemsUseCase: DeleteWishlistCollectionItemsUseCase,
    private val deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase,
    private val deleteWishlistProgressUseCase: DeleteWishlistProgressUseCase,
    private val atcUseCase: AddToCartUseCase,
    private val addWishlistCollectionItemsUseCase: AddWishlistCollectionItemsUseCase,
    private val updateWishlistCollectionUseCase: UpdateWishlistCollectionUseCase,
    private val getWishlistCollectionSharingDataUseCase: GetWishlistCollectionSharingDataUseCase,
    private val getWishlistCollectionTypeUseCase: GetWishlistCollectionTypeUseCase,
    private val addWishlistBulkUseCase: AddWishlistBulkUseCase,
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
    private val affiliateCookieHelper: AffiliateCookieHelper
) : BaseViewModel(dispatcher.main) {
    private var recommSrc = ""

    private val _collectionItems =
        MutableLiveData<Result<GetWishlistCollectionItemsResponse>>()
    val collectionItems: LiveData<Result<GetWishlistCollectionItemsResponse>>
        get() = _collectionItems

    private val _collectionData = MutableLiveData<Result<List<WishlistV2TypeLayoutData>>>()
    val collectionData: LiveData<Result<List<WishlistV2TypeLayoutData>>>
        get() = _collectionData

    private val _deleteWishlistV2Result =
        MutableLiveData<Result<com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response.Data.WishlistRemoveV2>>()
    val deleteWishlistV2Result: LiveData<Result<com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _deleteWishlistV2Result

    private val _bulkDeleteWishlistV2Result =
        MutableLiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>()
    val bulkDeleteWishlistV2Result: LiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>
        get() = _bulkDeleteWishlistV2Result

    private val _deleteCollectionItemsResult =
        MutableLiveData<Result<DeleteWishlistCollectionItemsResponse.DeleteWishlistCollectionItems>>()
    val deleteCollectionItemsResult: LiveData<Result<DeleteWishlistCollectionItemsResponse.DeleteWishlistCollectionItems>>
        get() = _deleteCollectionItemsResult

    private val _deleteCollectionResult =
        MutableLiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>()
    val deleteCollectionResult: LiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>
        get() = _deleteCollectionResult

    private val _deleteWishlistProgressResult =
        MutableLiveData<Result<DeleteWishlistProgressResponse.DeleteWishlistProgress>>()
    val deleteWishlistProgressResult: LiveData<Result<DeleteWishlistProgressResponse.DeleteWishlistProgress>>
        get() = _deleteWishlistProgressResult

    private val _atcResult = MutableLiveData<Result<AddToCartDataModel>>()
    val atcResult: LiveData<Result<AddToCartDataModel>>
        get() = _atcResult

    private val _addWishlistCollectionItem =
        MutableLiveData<Result<AddWishlistCollectionItemsResponse.AddWishlistCollectionItems>>()
    val addWishlistCollectionItem: LiveData<Result<AddWishlistCollectionItemsResponse.AddWishlistCollectionItems>>
        get() = _addWishlistCollectionItem

    private val _updateWishlistCollectionResult =
        MutableLiveData<Result<UpdateWishlistCollectionResponse.UpdateWishlistCollection>>()
    val updateWishlistCollectionResult: LiveData<Result<UpdateWishlistCollectionResponse.UpdateWishlistCollection>>
        get() = _updateWishlistCollectionResult

    private val _getWishlistCollectionSharingDataResult =
        MutableLiveData<Result<GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData>>()
    val getWishlistCollectionSharingDataResult: LiveData<Result<GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData>>
        get() = _getWishlistCollectionSharingDataResult

    private val _collectionType =
        MutableLiveData<Result<GetWishlistCollectionTypeResponse.GetWishlistCollectionItems>>()
    val collectionType: LiveData<Result<GetWishlistCollectionTypeResponse.GetWishlistCollectionItems>>
        get() = _collectionType

    private val _addWishlistBulkResult =
        MutableLiveData<Result<AddWishlistBulkResponse.AddWishlistBulk>>()
    val addWishlistBulkResult: LiveData<Result<AddWishlistBulkResponse.AddWishlistBulk>>
        get() = _addWishlistBulkResult

    fun getWishlistCollectionItems(
        params: GetWishlistCollectionItemsParams,
        typeLayout: String?,
        isAutomaticDelete: Boolean
    ) {
        WishlistIdlingResource.increment()
        launchCatchError(block = {
            val result = getWishlistCollectionItemsUseCase(params)
            recommSrc =
                if (result.getWishlistCollectionItems.totalData == 0) EMPTY_WISHLIST_PAGE_NAME else WISHLIST_PAGE_NAME
            _collectionItems.value = Success(result)
            _collectionData.value = Success(
                organizeWishlistV2Data(
                    convertCollectionItemsIntoWishlistUiModel(result.getWishlistCollectionItems),
                    typeLayout,
                    isAutomaticDelete,
                    getRecommendationWishlistV2(
                        1,
                        listOf(),
                        recommSrc
                    ),
                    getTopAdsData(),
                    true
                )
            )
            WishlistIdlingResource.decrement()
        }, onError = {
                _collectionItems.value = Fail(it)
                WishlistIdlingResource.decrement()
            })
    }

    fun loadRecommendation(page: Int) {
        WishlistIdlingResource.increment()
        val listData = arrayListOf<WishlistV2TypeLayoutData>()
        launch {
            try {
                val recommItems = getRecommendationWishlistV2(
                    page,
                    listOf(),
                    recommSrc
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
                WishlistIdlingResource.decrement()
            } catch (e: Exception) {
                Timber.d(e)
                WishlistIdlingResource.decrement()
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
            recommendation.recommendationItemList,
            recommendation.title
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
        additionalParams: WishlistV2BulkRemoveAdditionalParams,
        source: String
    ) {
        launch {
            _bulkDeleteWishlistV2Result.value = bulkDeleteWishlistV2UseCase.executeSuspend(
                listProductId,
                userId,
                mode,
                additionalParams,
                source
            )
        }
    }

    fun deleteWishlistCollectionItems(listProductId: List<String>) {
        launchCatchError(block = {
            val result = deleteCollectionItemsUseCase(listProductId)
            if (result.deleteWishlistCollectionItems.status == WishlistV2CommonConsts.OK && result.deleteWishlistCollectionItems.errorMessage.isEmpty()) {
                _deleteCollectionItemsResult.value = Success(result.deleteWishlistCollectionItems)
            } else {
                _deleteCollectionItemsResult.value = Fail(Throwable())
            }
        }, onError = {
                _deleteCollectionItemsResult.value = Fail(it)
            })
    }

    fun deleteWishlistCollection(collectionId: String) {
        launchCatchError(block = {
            val result = deleteWishlistCollectionUseCase(collectionId)
            if (result.deleteWishlistCollection.status == WishlistV2CommonConsts.OK && result.deleteWishlistCollection.errorMessage.isEmpty()) {
                _deleteCollectionResult.value = Success(result.deleteWishlistCollection)
            } else {
                _deleteCollectionResult.value = Fail(Throwable())
            }
        }, onError = {
                _deleteCollectionResult.value = Fail(it)
            })
    }

    fun getDeleteWishlistProgress() {
        launchCatchError(block = {
            val result = deleteWishlistProgressUseCase(Unit)
            if (result.deleteWishlistProgress.status == WishlistV2CommonConsts.OK && result.deleteWishlistProgress.errorMessage.isEmpty()) {
                _deleteWishlistProgressResult.value = Success(result.deleteWishlistProgress)
            } else {
                _deleteWishlistProgressResult.value = Fail(Throwable())
            }
        }, onError = {
                _deleteWishlistProgressResult.value = Fail(it)
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

    fun saveNewWishlistCollection(addWishlistParam: AddWishlistCollectionsHostBottomSheetParams) {
        launchCatchError(block = {
            val result = addWishlistCollectionItemsUseCase(addWishlistParam)
            if (result.addWishlistCollectionItems.status == WishlistV2CommonConsts.OK && result.addWishlistCollectionItems.errorMessage.isEmpty()) {
                _addWishlistCollectionItem.value = Success(result.addWishlistCollectionItems)
            } else {
                _addWishlistCollectionItem.value = Fail(Throwable())
            }
        }, onError = {
                _addWishlistCollectionItem.value = Fail(it)
            })
    }

    fun updateAccessWishlistCollection(updateWishlistCollectionParams: UpdateWishlistCollectionParams) {
        launchCatchError(block = {
            val result = updateWishlistCollectionUseCase(updateWishlistCollectionParams)
            if (result.updateWishlistCollection.status == WishlistV2CommonConsts.OK && result.updateWishlistCollection.data.success) {
                _updateWishlistCollectionResult.value = Success(result.updateWishlistCollection)
            } else {
                _updateWishlistCollectionResult.value = Fail(Throwable())
            }
        }, onError = {
                _updateWishlistCollectionResult.value = Fail(it)
            })
    }

    fun getWishlistCollectionSharingData(collectionId: Long) {
        launchCatchError(block = {
            val result = getWishlistCollectionSharingDataUseCase(collectionId)
            if (result.getWishlistCollectionSharingData.status == WishlistV2CommonConsts.OK) {
                _getWishlistCollectionSharingDataResult.value =
                    Success(result.getWishlistCollectionSharingData)
            } else {
                _getWishlistCollectionSharingDataResult.value = Fail(Throwable())
            }
        }, onError = {
                _getWishlistCollectionSharingDataResult.value = Fail(it)
            })
    }

    fun getWishlistCollectionType(collectionId: String) {
        launchCatchError(block = {
            val result = getWishlistCollectionTypeUseCase(collectionId)
            _collectionType.value = Success(result.getWishlistCollectionItems)
        }, onError = {
                _collectionType.value = Fail(it)
            })
    }

    fun addWishlistBulk(params: AddWishlistBulkParams) {
        launchCatchError(block = {
            val result = addWishlistBulkUseCase(params)
            _addWishlistBulkResult.value = Success(result.addWishlistBulk)
        }, onError = {
                _addWishlistBulkResult.value = Fail(it)
            })
    }

    fun addWishListV2(
        productId: String,
        userId: String,
        listener: WishlistV2ActionListener,
        sourceCollectionId: String
    ) {
        launch(dispatcher.main) {
            addToWishlistV2UseCase.setParams(productId, userId, sourceCollectionId)
            val result =
                withContext(dispatcher.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                listener.onSuccessAddWishlist(result.data, productId)
            } else {
                listener.onErrorAddWishList((result as Fail).throwable, productId)
            }
        }
    }

    fun hitAffiliateCookie(
        affiliateUuid: String,
        affiliateChannel: String,
        wishlistCollectionId: String
    ) {
        launchCatchError(block = {
            affiliateCookieHelper.initCookie(
                affiliateUUID = affiliateUuid,
                affiliateChannel = affiliateChannel,
                affiliatePageDetail = AffiliatePageDetail(
                    pageId = wishlistCollectionId,
                    source = AffiliateSdkPageSource.Wishlist()
                )
            )
        }, onError = {
                // no op, expect to be handled by Affiliate SDK
            })
    }

    fun checkShouldCreateAffiliateCookieAtcProduct(
        affiliateUUID: String,
        affiliateChannel: String,
        wishlistItemOnAtc: WishlistV2UiModel.Item
    ) {
        if (affiliateUUID.isNotEmpty()) {
            launchCatchError(block = {
                val affiliateSource = AffiliateSdkPageSource.DirectATC(
                    AffiliateAtcSource.SHOP_PAGE,
                    wishlistItemOnAtc.shop.id,
                    null
                )
                affiliateCookieHelper.initCookie(
                    affiliateUUID = affiliateUUID,
                    affiliateChannel = affiliateChannel,
                    affiliatePageDetail = AffiliatePageDetail(
                        pageId = wishlistItemOnAtc.id,
                        source = affiliateSource
                    )
                )
            }, onError = {
                    // no op, expect to be handled by Affiliate SDK
                })
        }
    }

    fun createAffiliateLink(url: String): String {
        return affiliateCookieHelper.createAffiliateLink(url)
    }

    companion object {
        private const val WISHLIST_PAGE_NAME = "wishlist"
        private const val EMPTY_WISHLIST_PAGE_NAME = "empty_wishlist"
    }
}
