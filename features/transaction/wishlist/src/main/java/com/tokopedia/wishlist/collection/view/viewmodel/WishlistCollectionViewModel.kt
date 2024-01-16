package com.tokopedia.wishlist.collection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.collection.data.response.AffiliateUserDetailOnBoardingBottomSheetResponse
import com.tokopedia.wishlist.collection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlist.collection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlist.collection.data.response.GetWishlistCollectionSharingDataResponse
import com.tokopedia.wishlist.collection.domain.AffiliateUserDetailOnBoardingBottomSheetUseCase
import com.tokopedia.wishlist.collection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlist.collection.domain.GetWishlistCollectionSharingDataUseCase
import com.tokopedia.wishlist.collection.domain.GetWishlistCollectionUseCase
import com.tokopedia.wishlist.collection.util.WishlistCollectionUtils
import com.tokopedia.wishlist.detail.data.model.WishlistCollectionState
import com.tokopedia.wishlist.detail.data.model.WishlistRecommendationDataModel
import com.tokopedia.wishlist.detail.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.detail.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlist.detail.util.WishlistConsts
import com.tokopedia.wishlist.detail.util.WishlistIdlingResource
import com.tokopedia.wishlist.detail.util.WishlistUtils
import com.tokopedia.wishlistcommon.data.params.UpdateWishlistCollectionParams
import com.tokopedia.wishlistcommon.data.response.UpdateWishlistCollectionResponse
import com.tokopedia.wishlistcommon.domain.UpdateWishlistCollectionUseCase
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class WishlistCollectionViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionUseCase: GetWishlistCollectionUseCase,
    private val deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase,
    private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
    private val deleteWishlistProgressUseCase: DeleteWishlistProgressUseCase,
    private val getWishlistCollectionSharingDataUseCase: GetWishlistCollectionSharingDataUseCase,
    private val affiliateUserDetailOnBoardingBottomSheetUseCase: AffiliateUserDetailOnBoardingBottomSheetUseCase,
    private val updateWishlistCollectionUseCase: UpdateWishlistCollectionUseCase
) : BaseViewModel(dispatcher.main) {
    private var recommSrc = ""

    private val _collections =
        MutableLiveData<Result<GetWishlistCollectionResponse.GetWishlistCollections>>()
    val collections: LiveData<Result<GetWishlistCollectionResponse.GetWishlistCollections>>
        get() = _collections

    private val _collectionData = MutableLiveData<WishlistCollectionState>()
    val collectionData: LiveData<WishlistCollectionState>
        get() = _collectionData

    private val _deleteCollectionResult =
        MutableLiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>()
    val deleteCollectionResult: LiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>
        get() = _deleteCollectionResult

    private val _deleteWishlistProgressResult =
        MutableLiveData<Result<DeleteWishlistProgressResponse.DeleteWishlistProgress>>()
    val deleteWishlistProgressResult: LiveData<Result<DeleteWishlistProgressResponse.DeleteWishlistProgress>>
        get() = _deleteWishlistProgressResult

    private val _getWishlistCollectionSharingDataResult =
        MutableLiveData<Result<GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData>>()
    val getWishlistCollectionSharingDataResult: LiveData<Result<GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData>>
        get() = _getWishlistCollectionSharingDataResult

    private val _updateWishlistCollectionResult =
        MutableLiveData<Result<UpdateWishlistCollectionResponse.UpdateWishlistCollection>>()
    val updateWishlistCollectionResult: LiveData<Result<UpdateWishlistCollectionResponse.UpdateWishlistCollection>>
        get() = _updateWishlistCollectionResult

    private val _isUserAffiliate =
        MutableLiveData<Result<AffiliateUserDetailOnBoardingBottomSheetResponse.AffiliateUserDetail>>()
    val isUserAffiliate: MutableLiveData<Result<AffiliateUserDetailOnBoardingBottomSheetResponse.AffiliateUserDetail>>
        get() = _isUserAffiliate

    fun getWishlistCollections() {
        WishlistIdlingResource.increment()
        launchCatchError(block = {
            _collectionData.value = WishlistCollectionState.InitialLoading
            val result = getWishlistCollectionUseCase(Unit)
            if (result.getWishlistCollections.status == OK && result.getWishlistCollections.errorMessage.isEmpty()) {
                recommSrc =
                    if (result.getWishlistCollections.data.isEmptyState) EMPTY_WISHLIST_PAGE_NAME else WISHLIST_PAGE_NAME
                _collections.value = Success(result.getWishlistCollections)
                _collectionData.value =
                    WishlistCollectionState.Set(WishlistCollectionUtils.mapCollection(result.getWishlistCollections.data))

                val recommendationResult = getRecommendationWishlistV2(1, listOf(), recommSrc)
                _collectionData.value =
                    WishlistCollectionState.Set(
                        WishlistCollectionUtils.mapCollection(
                            result.getWishlistCollections.data,
                            recommendationResult
                        )
                    )
            } else {
                _collections.value = Fail(Throwable())
                _collectionData.value = WishlistCollectionState.Error(Throwable())
            }
            WishlistIdlingResource.decrement()
        }, onError = {
                _collections.value = Fail(it)
                _collectionData.value = WishlistCollectionState.Error(Throwable())
                WishlistIdlingResource.decrement()
            })
    }

    fun deleteWishlistCollection(collectionId: String) {
        launchCatchError(block = {
            val result = deleteWishlistCollectionUseCase(collectionId)
            if (result.deleteWishlistCollection.status == OK && result.deleteWishlistCollection.errorMessage.isEmpty()) {
                _deleteCollectionResult.value = Success(result.deleteWishlistCollection)
            } else {
                _deleteCollectionResult.value = Fail(Throwable())
            }
        }, onError = {
                _deleteCollectionResult.value = Fail(it)
            })
    }

    suspend fun getRecommendationWishlistV2(
        page: Int,
        productIds: List<String>,
        pageName: String
    ): WishlistRecommendationDataModel {
        try {
            val recommendation = singleRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageNumber = page,
                    productIds = productIds,
                    pageName = pageName
                )
            )
            if (recommendation.recommendationItemList.isEmpty()) {
                return WishlistRecommendationDataModel()
            } else {
                return WishlistRecommendationDataModel(
                    WishlistUtils.convertRecommendationIntoProductDataModel(recommendation.recommendationItemList),
                    recommendation.recommendationItemList,
                    recommendation.title
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
            return WishlistRecommendationDataModel()
        }
    }

    fun loadRecommendation(page: Int) {
        WishlistIdlingResource.increment()
        val listData = arrayListOf<WishlistCollectionTypeLayoutData>()
        launch {
            try {
                val recommItems = getRecommendationWishlistV2(
                    page,
                    listOf(),
                    recommSrc
                )
                recommItems.recommendationProductCardModelData.forEach { item ->
                    listData.add(
                        WishlistCollectionTypeLayoutData(
                            "${WishlistConsts.TYPE_RECOMMENDATION_LIST}_${item.productName}",
                            item,
                            WishlistConsts.TYPE_RECOMMENDATION_LIST
                        )
                    )
                }
                _collectionData.value = WishlistCollectionState.Update(listData)
                WishlistIdlingResource.decrement()
            } catch (e: Exception) {
                Timber.d(e)
                WishlistIdlingResource.decrement()
            }
        }
    }

    fun getDeleteWishlistProgress() {
        launchCatchError(block = {
            val result = deleteWishlistProgressUseCase(Unit)
            if (result.deleteWishlistProgress.status == OK && result.deleteWishlistProgress.errorMessage.isEmpty()) {
                _deleteWishlistProgressResult.value = Success(result.deleteWishlistProgress)
            } else {
                _deleteWishlistProgressResult.value = Fail(Throwable())
            }
        }, onError = {
                _deleteWishlistProgressResult.value = Fail(it)
            })
    }

    fun getWishlistCollectionSharingData(collectionId: Long) {
        launchCatchError(block = {
            val result = getWishlistCollectionSharingDataUseCase(collectionId)
            if (result.getWishlistCollectionSharingData.status == OK) {
                _getWishlistCollectionSharingDataResult.value =
                    Success(result.getWishlistCollectionSharingData)
            } else {
                _getWishlistCollectionSharingDataResult.value = Fail(Throwable())
            }
        }, onError = {
                _getWishlistCollectionSharingDataResult.value = Fail(it)
            })
    }

    fun updateAccessWishlistCollection(updateWishlistCollectionParams: UpdateWishlistCollectionParams) {
        launchCatchError(block = {
            val result = updateWishlistCollectionUseCase(updateWishlistCollectionParams)
            if (result.updateWishlistCollection.status == OK && result.updateWishlistCollection.data.success) {
                _updateWishlistCollectionResult.value = Success(result.updateWishlistCollection)
            } else {
                _updateWishlistCollectionResult.value = Fail(Throwable())
            }
        }, onError = {
                _updateWishlistCollectionResult.value = Fail(it)
            })
    }

    fun getAffiliateUserDetail() {
        launchCatchError(
            block = {
                val result = affiliateUserDetailOnBoardingBottomSheetUseCase(Unit)
                isUserAffiliate.value = Success(result.affiliateUserDetail)
            },
            onError = {
                isUserAffiliate.value = Fail(it)
            }
        )
    }

    companion object {
        private const val WISHLIST_PAGE_NAME = "wlcollection"
        private const val EMPTY_WISHLIST_PAGE_NAME = "wlcollection_empty"
    }
}
