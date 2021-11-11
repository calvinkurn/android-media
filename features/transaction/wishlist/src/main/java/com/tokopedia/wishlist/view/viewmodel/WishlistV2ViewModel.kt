package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.DeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.ext.mappingRecommendationToWishlist
import com.tokopedia.wishlist.ext.mappingTopadsBannerToWishlist
import com.tokopedia.wishlist.ext.mappingTopadsBannerWithRecommendationToWishlist
import com.tokopedia.wishlist.ext.mappingWishlistToVisitable
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by fwidjaja on 15/10/21.
 */
class WishlistV2ViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                              private val wishlistV2UseCase: WishlistV2UseCase,
                                              private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
                                              private val bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase,
                                              private val recommendationUseCase: GetRecommendationUseCase,
                                              private val atcUseCase: AddToCartUseCase) : BaseViewModel(dispatcher.main) {
class WishlistV2ViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val wishlistV2UseCase: WishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val recommendationUseCase: GetRecommendationUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val singleRecommendationUseCase: GetSingleRecommendationUseCase
) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2Result = MutableLiveData<Result<List<WishlistV2Data>>>()
    val wishlistV2Result: LiveData<Result<List<WishlistV2Data>>>
        get() = _wishlistV2Result

    private val _wishlistData = MutableLiveData<WishlistV2Response.Data.WishlistV2>()
    val wishlistData: LiveData<WishlistV2Response.Data.WishlistV2>
        get() = _wishlistData

    private val _deleteWishlistV2Result =
        MutableLiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>()
    val deleteWishlistV2Result: LiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _deleteWishlistV2Result

    private val _bulkDeleteWishlistV2Result = MutableLiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>()
    val bulkDeleteWishlistV2Result: LiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>
        get() = _bulkDeleteWishlistV2Result

    private val _atcResult = MutableLiveData<Result<AddToCartDataModel>>()
    val atcResult: LiveData<Result<AddToCartDataModel>>
        get() = _atcResult

    fun loadWishlistV2(params: WishlistV2Params) {
        launch(dispatcher.main) {
            try {
                val wishlistData = wishlistV2UseCase.executeSuspend(params).wishlistV2
                if (wishlistData.items.isEmpty()) {
                    val recommendationData = singleRecommendationUseCase.getData(
                        GetRecommendationRequestParam(pageNumber = 0, pageName = "empty_wishlist")
                    )
                    _wishlistV2Result.value =
                        Success(
                            listOf(
                                WishlistV2EmptyDataModel(query = wishlistData.query),
                                WishlistV2RecommendationDataModel(
                                    recommendationData = listOf(recommendationData), false
                                )
                            )
                        )
                } else {
                    _wishlistData.value = wishlistData
                    val visitableWishlist = wishlistData.items.mappingWishlistToVisitable()
                    when {
                        wishlistData.items.size < maxItemInPage -> {
                            _wishlistV2Result.value = Success(
                                getRecommendationWishlist(
                                    visitableWishlist,
                                    wishlistData.page,
                                    wishlistData.items.map { it.id },
                                    wishlistData.items.size
                                )
                            )
                        }

                        // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                        wishlistData.items.size == maxItemInPage -> {
                            _wishlistV2Result.value = Success(
                                getTopadsAndRecommendationWishlist(
                                    visitableWishlist,
                                    wishlistData.page,
                                    wishlistData.items.map { it.id },
                                    wishlistData.items.size
                                    )
                            )
                        }

                        // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
                        wishlistData.items.size > maxItemInPage -> {
                            if (wishlistData.totalData > newMinItemRegularRule) {
                                _wishlistV2Result.value = Success(
                                    getTopAdsBannerData(
                                        visitableWishlist,
                                        wishlistData.page,
                                        wishlistData.items.map { it.id },
                                        topAdsPositionInPage
                                    )
                                )
                            } else {
                                _wishlistV2Result.value = Success(
                                    getTopadsAndRecommendationWishlist(
                                        visitableWishlist,
                                        wishlistData.page,
                                        wishlistData.items.map { it.id },
                                        wishlistData.items.size
                                    )
                                )
                            }
                        }
                    }


                }

            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e)
            }
        }
    }

    fun deleteWishlistV2(productId: String, userId: String) {
        launch {
            _deleteWishlistV2Result.value =
                deleteWishlistV2UseCase.executeSuspend(productId, userId)
        }
    }

    fun bulkDeleteWishlistV2(listProductId: List<String>, userId: String) {
        launch {
            _bulkDeleteWishlistV2Result.value = bulkDeleteWishlistV2UseCase.executeSuspend(listProductId, userId)
        }
    }

    fun getRecommendationOnEmptyWishlist(page: Int) {
        launch {
            try {
                val widget = singleRecommendationUseCase.getData(
                    GetRecommendationRequestParam(pageNumber = page, pageName = "empty_wishlist")
                )
                _wishlistV2Result.value =
                    Success(listOf(WishlistV2RecommendationDataModel(listOf(widget), false)))
            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e.fillInStackTrace())
            }
        }
    }

    private suspend fun getRecommendationWishlist(
        wishlistVisitable: List<WishlistV2Data>,
        page: Int,
        productIds: List<String>,
        recomIndex: Int
    ): List<WishlistV2Data> =
        withContext(dispatcher.io) {
            try {
                val recommendationData = recommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = page,
                        productIds = productIds,
                        pageName = WISHLIST_PAGE_NAME
                    )
                )
                if (recommendationData.isNotEmpty()) {
                    return@withContext recommendationData.mappingRecommendationToWishlist(
                        currentPage = page,
                        wishlistVisitable = wishlistVisitable,
                        recommendationPositionInPage = recomIndex,
                        maxItemInPage = maxItemInPage,
                        topAdsPositionInPage = topAdsPositionInPage
                    )
                }
                return@withContext wishlistVisitable
            } catch (e: Throwable) {
                return@withContext wishlistVisitable
            }
        }

    private suspend fun getTopadsAndRecommendationWishlist(
        wishlistVisitable: List<WishlistV2Data>,
        page: Int,
        productIds: List<String>,
        recomIndex: Int
    ): List<WishlistV2Data> =
        withContext(dispatcher.io) {
            try {
                if (wishlistVisitable.isNotEmpty()) {
                    val recommendationPositionInPreviousPage =
                        ((page - RECOM_INDEX_STARTER_MINUS) * maxItemInPage) + recommendationPositionInPage
                    var pageToken = ""
                    if (recommendationPositionInPreviousPage >= 0 && wishlistVisitable.getOrNull(
                            recommendationPositionInPreviousPage
                        ) is WishlistV2TopAdsDataModel
                    ) {
                        pageToken =
                            (wishlistVisitable[recommendationPositionInPreviousPage] as WishlistV2TopAdsDataModel).topAdsData.nextPageToken
                                ?: ""
                    }
                    val topadsResult = topAdsImageViewUseCase.getImageData(
                        topAdsImageViewUseCase.getQueryMap(
                            "",
                            WISHLIST_TOPADS_SOURCE,
                            pageToken,
                            WISHLIST_TOPADS_ADS_COUNT,
                            WISHLIST_TOPADS_DIMENS,
                            ""
                        )
                    )

                    val recommendationResult = recommendationUseCase.getData(
                        GetRecommendationRequestParam(
                            pageNumber = page,
                            productIds = productIds,
                            pageName = WISHLIST_PAGE_NAME
                        )
                    )

                    if (topadsResult.isNotEmpty() && recommendationResult.isNotEmpty()) {
                        return@withContext mappingTopadsBannerWithRecommendationToWishlist(
                            topadsBanner = topadsResult.first(),
                            wishlistVisitable = wishlistVisitable,
                            listRecommendation = recommendationResult,
                            recommendationPositionInPage = recommendationPositionInPage,
                            currentPage = page,
                            isInBulkMode = false,
                            maxItemInPage = maxItemInPage,
                            recommendationIndex = recomIndex + 1,
                            topAdsPositionInPage = topAdsPositionInPage
                        )
                    } else if (recommendationResult.isNotEmpty()) {
                        return@withContext recommendationResult.mappingRecommendationToWishlist(
                            currentPage = page,
                            wishlistVisitable = wishlistVisitable,
                            recommendationPositionInPage = recomIndex,
                            maxItemInPage = maxItemInPage,
                            topAdsPositionInPage = topAdsPositionInPage
                        )
                    }
                }
                return@withContext wishlistVisitable
            } catch (e: Throwable) {
                return@withContext wishlistVisitable
            }
        }

    private suspend fun getTopAdsBannerData(
        wishlistVisitable: List<WishlistV2Data>,
        currentPage: Int,
        productIds: List<String>,
        topAdsIndex: Int
    ): List<WishlistV2Data> {
        return withContext(dispatcher.io) {
            try {
                if (wishlistVisitable.isNotEmpty()) {
                    val recommendationPositionInPreviousPage =
                        ((currentPage - RECOM_INDEX_STARTER_MINUS) * maxItemInPage) + recommendationPositionInPage
                    var pageToken = ""
                    if (recommendationPositionInPreviousPage >= 0 && wishlistVisitable.getOrNull(
                            recommendationPositionInPreviousPage
                        ) is WishlistV2TopAdsDataModel
                    ) {
                        pageToken =
                            (wishlistVisitable[recommendationPositionInPreviousPage] as WishlistV2TopAdsDataModel).topAdsData.nextPageToken
                                ?: ""
                    }
                    val results = topAdsImageViewUseCase.getImageData(
                        topAdsImageViewUseCase.getQueryMap(
                            "",
                            WISHLIST_TOPADS_SOURCE,
                            pageToken,
                            WISHLIST_TOPADS_ADS_COUNT,
                            WISHLIST_TOPADS_DIMENS,
                            ""
                        )
                    )
                    if (results.isNotEmpty()) {
                        return@withContext wishlistVisitable.mappingTopadsBannerToWishlist(
                            topadsBanner = results.first(),
                            topAdsPositionInPage = topAdsPositionInPage,
                            currentPage = currentPage,
                            maxItemInPage = maxItemInPage,
                            wishlistItemInList = wishlistVisitable.count { element -> element is WishlistV2DataModel }
                        )
                    } else {
                        return@withContext getRecommendationWishlist(
                            wishlistVisitable = wishlistVisitable,
                            page = currentPage,
                            productIds = productIds,
                            recomIndex = topAdsIndex
                        )
                    }
                }
                return@withContext wishlistVisitable
            } catch (e: Throwable) {
                return@withContext wishlistVisitable
            }
        }
    }

    fun getNextPageWishlistData(param: WishlistV2Params) {
        launch {
            try {
                val data = wishlistV2UseCase.executeSuspend(param).wishlistV2

                val previousData = if (_wishlistV2Result.value is Success) {
                    (_wishlistV2Result.value as Success<List<WishlistV2Data>>).data.toMutableList()
                } else {
                    mutableListOf()
                }

                val newPageVisitableData = mutableListOf<WishlistV2Data>().apply {
                    addAll(previousData)
                    addAll(data.items.mappingWishlistToVisitable())
                }

                _wishlistV2Result.value = Success(organizeWishlistV2Data(newPageVisitableData, param.page, data.items.map{it -> it.id}))

            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e.fillInStackTrace())
            }
        }
    }

    private suspend fun organizeWishlistV2Data(wishlistVisitable: MutableList<WishlistV2Data>, page:Int, productIds: List<String>): List<WishlistV2Data> {
        return withContext(dispatcher.io) {
            val lastIndexOfTopAds = wishlistVisitable.indexOfLast { element -> element is WishlistV2TopAdsDataModel }
            val pageToken = if (lastIndexOfTopAds != -1) {
                (wishlistVisitable[lastIndexOfTopAds] as WishlistV2TopAdsDataModel).topAdsData.nextPageToken
                    ?: ""
            } else {
                ""
            }
            val stepNextTopAds = topAdsPositionInPage + 1
            val nextIndexOfTopAds =
                if (lastIndexOfTopAds == -1) topAdsPositionInPage else lastIndexOfTopAds + stepNextTopAds
            for (i in nextIndexOfTopAds until wishlistVisitable.size step stepNextTopAds) {
                if (wishlistVisitable[i] !is WishlistV2TopAdsDataModel) {
                    val topAdsData = topAdsImageViewUseCase.getImageData(
                        topAdsImageViewUseCase.getQueryMap(
                            "",
                            WISHLIST_TOPADS_SOURCE,
                            pageToken,
                            WISHLIST_TOPADS_ADS_COUNT,
                            WISHLIST_TOPADS_DIMENS,
                            ""
                        )
                    )
                    wishlistVisitable.add(i, WishlistV2TopAdsDataModel(topAdsData = topAdsData.first()))
                }
            }
            if (wishlistVisitable.size >= newMinItemRegularRule) {
                val lastIndexOfRecommendation =
                    wishlistVisitable.indexOfLast { element -> element is WishlistV2RecommendationDataModel }
                val nextIndexOfRecommendation =
                    if (lastIndexOfRecommendation == -1) recommendationPositionInPage else lastIndexOfRecommendation + recommendationPositionInPage + 1
                if (nextIndexOfRecommendation <= wishlistVisitable.size) {
                    val recommendationResult = recommendationUseCase.getData(
                        GetRecommendationRequestParam(
                            pageNumber = page,
                            productIds = productIds,
                            pageName = WISHLIST_PAGE_NAME
                        )
                    )
                        wishlistVisitable.add(
                            nextIndexOfRecommendation,
                            WishlistV2RecommendationDataModel(recommendationResult, true)
                        )
                }
            }
            wishlistVisitable
        }
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

    companion object {
        private const val recommendationPositionInPage = 30
        private const val topAdsPositionInPage = 4
        private const val maxItemInPage = 4
        private const val newMinItemRegularRule = 24
        private const val WISHLIST_TOPADS_SOURCE = "6"
        private const val WISHLIST_TOPADS_ADS_COUNT = 1
        private const val WISHLIST_TOPADS_DIMENS = 3
        private const val WISHLIST_PAGE_NAME = "wishlist"

        private const val RECOM_INDEX_STARTER = 4
        private const val RECOM_INDEX_STARTER_MINUS = 3
    }
}