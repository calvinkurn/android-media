package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by fwidjaja on 15/10/21.
 */
class WishlistV2ViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val wishlistV2UseCase: WishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val recommendationUseCase: GetRecommendationUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2Result = MutableLiveData<Result<List<WishlistV2Data>>>()
    val wishlistV2Result: LiveData<Result<List<WishlistV2Data>>>
        get() = _wishlistV2Result

//    private val _recommendationResult = MutableLiveData<Result<List<RecommendationWidget>>>()
//    val recommendationResult: LiveData<Result<List<RecommendationWidget>>>
//        get() = _recommendationResult

    private val _deleteWishlistV2Result =
        MutableLiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>()
    val deleteWishlistV2Result: LiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _deleteWishlistV2Result

    private val _wishlistCount = MutableLiveData<Int>()
    val wishlistCount: LiveData<Int>
        get() = _wishlistCount


    fun loadWishlistV2(params: WishlistV2Params) {
        launch (dispatcher.main) {
//
//            if (shouldShowInitialPage) loadInitialPage()
//
//            keywordSearch = keyword
//            currentPage = 1
//            this.additionalParams = additionalParams
//
//            launchCatchError(wishlistCoroutineDispatcherProvider.main, block = {
//                val data = getWishlistUseCase.getData(
//                    GetWishlistParameter(keyword, currentPage, additionalParams))
//                if (!data.isSuccess) {
//                    isWishlistErrorInFirstPage.value = true
//
//                    wishlistData.value = listOf(ErrorWishlistDataModel(data.errorMessage))
//                    currentPage--
//                    return@launchCatchError
//                }
//
//                if(data.items.isEmpty()){
//                    wishlistState.value = Status.EMPTY
//
//                    wishlistData.value = listOf(
//                        if(keyword.isEmpty()) EmptyWishlistDataModel() else EmptySearchWishlistDataModel(keyword)
//                    )
//                    getRecommendationOnEmptyWishlist(0)
//                } else {
//                    wishlistCountData.value = data.totalData.toInt()
//                    wishlistState.value = Status.SUCCESS
//
//                    val visitableWishlist = data.items.mappingWishlistToVisitable(isInBulkMode.value ?: false)
//
//                    when {
//                        data.items.size < recommendationPositionInPage -> {
//                            wishlistData.value = getRecommendationWishlist(visitableWishlist, currentPage, data.items.map { it.id }, data.items.size)
//                        }
//
//                        // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
//                        data.items.size == recommendationPositionInPage -> {
//                            wishlistData.value = getTopadsAndRecommendationWishlist(visitableWishlist, currentPage, data.items.map { it.id }, data.items.size)
//                        }
//
//                        // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
//                        data.items.size > recommendationPositionInPage -> {
//                            if (data.totalData > newMinItemRegularRule) {
//                                wishlistData.value = getTopAdsBannerData(visitableWishlist, currentPage, data.items.map { it.id }, recommendationPositionInPage)
//                            } else {
//                                wishlistData.value = getTopadsAndRecommendationWishlist(visitableWishlist, currentPage, data.items.map { it.id }, data.items.size)
//                            }
//                        }
//                    } }

                    try {
                val wishlistData = wishlistV2UseCase.executeSuspend(params).wishlistV2

                if (wishlistData.items.isEmpty()) {
                    if (params.page == 1) {
                        val recommendationList = getRecomList(pageNumber = 0, false)
                        _wishlistV2Result.value = Success(
                            listOf(
                                recommendationList
                            )
                        )
                    }
                } else {
                    _wishlistCount.value = wishlistData.totalData
                    when {
                        wishlistData.items.size < recommendationPositionInPage -> {
                            if (!wishlistData.hasNextPage) {
                                val recommendationList = getRecomList(pageNumber = params.page, true)
                                _wishlistV2Result.value = Success(
                                    listOf(
                                        wishlistData,
                                        recommendationList
                                    )
                                )
                            } else {
                                _wishlistV2Result.value = Success(
                                    listOf(
                                        wishlistData
                                    )
                                )
                            }
                        }

                        // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                        wishlistData.items.size == recommendationPositionInPage -> {
                            val topAdsBanner = getTopAds(checkPageTokenForTopAds(listOf(wishlistData), params.page))
                            if (wishlistData.totalData == recommendationPositionInPage || !wishlistData.hasNextPage || (wishlistData.offset.plus(wishlistData.items.size) % 24 == 0)) {
                                val recommendationList = getRecomList(pageNumber = params.page, true)
                                _wishlistV2Result.value = Success(
                                    listOf(
                                        wishlistData,
                                        topAdsBanner,
                                        recommendationList
                                    )
                                )
                            } else {
                                _wishlistV2Result.value = Success(
                                    listOf(
                                        wishlistData,
                                        topAdsBanner
                                    )
                                )
                            }
                        }

                        // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
//                        wishlistData.items.size > recommendationPositionInPage -> {
//                            val topAdsBanner = getTopAds(checkPageTokenForTopAds(listOf(wishlistData), params.page))
//                            if (wishlistData.totalData > newMinItemRegularRule) {
//                                _wishlistV2Result.value =
//                                    Success(listOf(wishlistData, topAdsBanner))
//                            } else {
//                                val recommendationList = getRecomList(pageNumber = params.page, true)
//                                _wishlistV2Result.value = Success(
//                                    listOf(
//                                        wishlistData,
//                                        topAdsBanner,
//                                        recommendationList
//                                    )
//                                )
//                            }
//                        }
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

    fun loadRecommendationList(pageNumber: Int) {
        launch {
            try {
                val data = recommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = pageNumber,
                        pageName = WishlistV2Consts.PAGE_NAME
                    )
                )
                _wishlistV2Result.value = Success(listOf(WishlistV2RecommendationWrapper(data, false)))
            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e.fillInStackTrace())
            }
        }
    }

    private suspend fun getRecomList(pageNumber: Int, isCarousel: Boolean): WishlistV2RecommendationWrapper =
        withContext(dispatcher.io) {
            return@withContext WishlistV2RecommendationWrapper(
                recommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = pageNumber,
                        pageName = WishlistV2Consts.PAGE_NAME
                    )
                ), isCarousel = isCarousel
            )
        }

    private suspend fun getTopAds(pageToken: String): WishlistV2TopAdsWrapper =
        withContext(dispatcher.io) {
            return@withContext WishlistV2TopAdsWrapper(
                topAdsImageViewUseCase.getImageData(
                    topAdsImageViewUseCase.getQueryMap(
                        "",
                        WISHLIST_TOPADS_SOURCE,
                        pageToken,
                        WISHLIST_TOPADS_ADS_COUNT,
                        WISHLIST_TOPADS_DIMENS,
                        ""
                    )
                ).first()
            )
        }

    private fun checkPageTokenForTopAds(data: List<WishlistV2Data>, currentPage: Int): String {
        val recommendationPositionInPreviousPage =
            ((currentPage - RECOM_INDEX_STARTER_MINUS) * maxItemInPage) + recommendationPositionInPage
        return if (recommendationPositionInPreviousPage >= 0 && data.getOrNull(
                recommendationPositionInPreviousPage
            ) is WishlistV2TopAdsWrapper
        ) {
            (data[recommendationPositionInPreviousPage] as WishlistV2TopAdsWrapper).topAdsData.nextPageToken
                ?: ""
        } else ""
    }

    companion object {
        private const val recommendationPositionInPage = 4
        private const val maxItemInPage = 21
        private const val newMinItemRegularRule = 24
        private const val WISHLIST_TOPADS_SOURCE = "6"
        private const val WISHLIST_TOPADS_ADS_COUNT = 1
        private const val WISHLIST_TOPADS_DIMENS = 3

        private const val RECOM_INDEX_STARTER = 4
        private const val RECOM_INDEX_STARTER_MINUS = 3
    }
}