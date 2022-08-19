package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.domain.DeleteWishlistProgressUseCase
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.util.WishlistV2Utils
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.DeleteWishlistCollectionResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.domain.DeleteWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionUseCase
import com.tokopedia.wishlistcollection.util.WishlistCollectionUtils
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import kotlinx.coroutines.launch
import javax.inject.Inject

class WishlistCollectionViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionUseCase: GetWishlistCollectionUseCase,
    private val deleteWishlistCollectionUseCase: DeleteWishlistCollectionUseCase,
    private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
    private val deleteWishlistProgressUseCase: DeleteWishlistProgressUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collections =
        MutableLiveData<Result<GetWishlistCollectionResponse.GetWishlistCollections>>()
    val collections: LiveData<Result<GetWishlistCollectionResponse.GetWishlistCollections>>
        get() = _collections

    private val _collectionData = MutableLiveData<Result<List<WishlistCollectionTypeLayoutData>>>()
    val collectionData: LiveData<Result<List<WishlistCollectionTypeLayoutData>>>
        get() = _collectionData

    private val _deleteCollectionResult =
        MutableLiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>()
    val deleteCollectionResult: LiveData<Result<DeleteWishlistCollectionResponse.DeleteWishlistCollection>>
        get() = _deleteCollectionResult

    private val _recommendationListResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val recommendationListResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _recommendationListResult

    private val _deleteWishlistProgressResult = MutableLiveData<Result<DeleteWishlistProgressResponse.DeleteWishlistProgress>>()
    val deleteWishlistProgressResult: LiveData<Result<DeleteWishlistProgressResponse.DeleteWishlistProgress>>
        get() = _deleteWishlistProgressResult

    fun getWishlistCollections() {
        launchCatchError(block = {
            val result = getWishlistCollectionUseCase(Unit)
            if (result.getWishlistCollections.status == OK && result.getWishlistCollections.errorMessage.isEmpty()) {
                _collections.postValue(Success(result.getWishlistCollections))
                _collectionData.postValue(
                    Success(
                        WishlistCollectionUtils.mapCollection(
                            result.getWishlistCollections.data, getRecommendationWishlistV2(
                                1, listOf(),
                                WishlistV2Consts.EMPTY_WISHLIST_PAGE_NAME
                            )
                        )
                    )
                )
            } else {
                _collections.postValue(Fail(Throwable()))
                _collectionData.postValue(Fail(Throwable()))
            }
        }, onError = {
            _collections.postValue(Fail(it))
            _collectionData.postValue(Fail(Throwable()))
        })
    }

    fun deleteWishlistCollection(collectionId: String) {
        launchCatchError(block = {
            val result = deleteWishlistCollectionUseCase(collectionId)
            if (result.deleteWishlistCollection.status == OK && result.deleteWishlistCollection.errorMessage.isEmpty()) {
                _deleteCollectionResult.postValue(Success(result.deleteWishlistCollection))
            } else {
                _deleteCollectionResult.postValue(Fail(Throwable()))
            }
        }, onError = {
            _deleteCollectionResult.postValue(Fail(it))
        })
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
            WishlistV2Utils.convertRecommendationIntoProductDataModel(recommendation.recommendationItemList),
            recommendation.recommendationItemList, recommendation.title
        )
    }

    fun loadRecommendation(page: Int) {
        val listData = arrayListOf<WishlistCollectionTypeLayoutData>()
        launch {
            try {
                val recommItems = getRecommendationWishlistV2(
                    page, listOf(),
                    WishlistV2Consts.EMPTY_WISHLIST_PAGE_NAME
                )
                recommItems.recommendationProductCardModelData.forEach { item ->
                    listData.add(
                        WishlistCollectionTypeLayoutData(
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

    fun getDeleteWishlistProgress() {
        launchCatchError(block = {
            val result = deleteWishlistProgressUseCase(Unit)
            if (result.deleteWishlistProgress.status == WishlistV2CommonConsts.OK && result.deleteWishlistProgress.errorMessage.isEmpty()) {
                _deleteWishlistProgressResult.postValue(Success(result.deleteWishlistProgress))
            } else {
                _deleteWishlistProgressResult.postValue(Fail(Throwable()))
            }
        }, onError = {
            _deleteWishlistProgressResult.postValue(Fail(it))
        })
    }
}