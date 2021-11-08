package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.ext.mappingRecommendationToWishlist
import com.tokopedia.wishlist.ext.mappingTopadsBannerToWishlist
import com.tokopedia.wishlist.ext.mappingTopadsBannerWithRecommendationToWishlist
import com.tokopedia.wishlist.ext.mappingWishlistToVisitable
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
    private val singleRecommendationUseCase: GetSingleRecommendationUseCase
) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2Result = MutableLiveData<Result<List<WishlistV2Data>>>()
    val wishlistV2Result: LiveData<Result<List<WishlistV2Data>>>
        get() = _wishlistV2Result

    private val _wishlistData = MutableLiveData<WishlistV2Response.Data.WishlistV2>()
    val wishlistData: LiveData<WishlistV2Response.Data.WishlistV2>
        get() = _wishlistData

//    private val _recommendationResult = MutableLiveData<Result<List<RecommendationWidget>>>()
//    val recommendationResult: LiveData<Result<List<RecommendationWidget>>>
//        get() = _recommendationResult

    private val _deleteWishlistV2Result =
        MutableLiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>()
    val deleteWishlistV2Result: LiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _deleteWishlistV2Result


    fun loadWishlistV2(params: WishlistV2Params) {
        launch(dispatcher.main) {
            try {
                val wishlistData = wishlistV2UseCase.executeSuspend(params).wishlistV2

                if (wishlistData.items.isEmpty()) {
                    _wishlistV2Result.value =
                        Success(listOf(WishlistV2EmptyWrapper(query = wishlistData.query)))
                    getRecommendationOnEmptyWishlist(0)
                } else {

                        _wishlistData.value = wishlistData

                    val visitableWishlist = wishlistData.items.mappingWishlistToVisitable()
                    when {
                        wishlistData.items.size < recommendationPositionInPage -> {
                            _wishlistV2Result.value = Success(getRecommendationWishlist(
                                visitableWishlist,
                                wishlistData.page,
                                wishlistData.items.map { it.id },
                                wishlistData.items.size
                            ))
                        }

                        // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                        wishlistData.items.size == recommendationPositionInPage -> {
                            _wishlistV2Result.value = Success(getTopadsAndRecommendationWishlist(
                                visitableWishlist,
                                params.page,
                                wishlistData.items.map { it.id },
                                wishlistData.items.size,

                            ))
                        }

                        // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
                        wishlistData.items.size > recommendationPositionInPage -> {
                            if (wishlistData.totalData > newMinItemRegularRule) {
                                _wishlistV2Result.value = Success(getTopAdsBannerData(
                                    visitableWishlist,
                                    params.page,
                                    wishlistData.items.map { it.id },
                                    recommendationPositionInPage)
                                )
                            } else {
                                _wishlistV2Result.value = Success(getTopadsAndRecommendationWishlist(
                                    visitableWishlist,
                                    params.page,
                                    wishlistData.items.map { it.id },
                                    wishlistData.items.size)
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

    fun loadRecommendationList(pageNumber: Int) {
        launch {
            try {
                val data = recommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = pageNumber,
                        pageName = WishlistV2Consts.PAGE_NAME
                    )
                )
                _wishlistV2Result.value =
                    Success(listOf(WishlistV2RecommendationWrapper(data, false)))
            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e.fillInStackTrace())
            }
        }
    }

    fun getRecommendationOnEmptyWishlist(page: Int) {
        launchCatchError(block = {
            val widget = singleRecommendationUseCase.getData(
                GetRecommendationRequestParam(pageNumber = page, pageName = "empty_wishlist")
            )
            _wishlistV2Result.value =
                Success(listOf(WishlistV2RecommendationWrapper(listOf(widget), false)))
        }) {
        }
    }

    private suspend fun getRecomList(
        pageNumber: Int,
        isCarousel: Boolean
    ): WishlistV2RecommendationWrapper =
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

    private suspend fun getRecommendationWishlist(wishlistVisitable: List<WishlistV2Data>, page: Int, productIds: List<String>, recomIndex: Int): List<WishlistV2Data> =
        withContext(dispatcher.io){
            try{
                val recommendationData = recommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = page,
                        productIds = productIds,
                        pageName = WISHLIST_PAGE_NAME
                    )
                )
                if(recommendationData.isNotEmpty()) {
                    return@withContext recommendationData.mappingRecommendationToWishlist(
                        currentPage = page,
                        wishlistVisitable = wishlistVisitable,
                        recommendationPositionInPage = recomIndex,
                        maxItemInPage = maxItemInPage,
                    )
                }
                return@withContext wishlistVisitable
            } catch (e: Throwable){
                return@withContext wishlistVisitable
            }
        }

    private suspend fun getTopadsAndRecommendationWishlist(wishlistVisitable: List<WishlistV2Data>, page: Int, productIds: List<String>, recomIndex: Int): List<WishlistV2Data> =
        withContext(dispatcher.io){
            try{
                if (wishlistVisitable.isNotEmpty()) {
                    val recommendationPositionInPreviousPage = ((page - RECOM_INDEX_STARTER_MINUS) * maxItemInPage) + recommendationPositionInPage
                    var pageToken = ""
                    if(recommendationPositionInPreviousPage >= 0 && wishlistVisitable.getOrNull(recommendationPositionInPreviousPage) is WishlistV2TopAdsWrapper){
                        pageToken = (wishlistVisitable[recommendationPositionInPreviousPage] as WishlistV2TopAdsWrapper).topAdsData.nextPageToken ?: ""
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
                            recommendationIndex = recomIndex+1
                        )
                    } else if (recommendationResult.isNotEmpty()) {
                        return@withContext recommendationResult.mappingRecommendationToWishlist(
                            currentPage = page,
                            wishlistVisitable = wishlistVisitable,
                            recommendationPositionInPage = recomIndex,
                            maxItemInPage = maxItemInPage,
                        )
                    }
                }
                return@withContext wishlistVisitable
            } catch (e: Throwable){
                return@withContext wishlistVisitable
            }
        }

    private suspend fun getTopAdsBannerData(wishlistVisitable: List<WishlistV2Data>, currentPage: Int, productIds: List<String>, topAdsIndex: Int): List<WishlistV2Data>{
        return withContext(dispatcher.io){
            try{
                if(wishlistVisitable.isNotEmpty()) {
                    val recommendationPositionInPreviousPage = ((currentPage - RECOM_INDEX_STARTER_MINUS) * maxItemInPage) + recommendationPositionInPage
                    var pageToken = ""
                    if(recommendationPositionInPreviousPage >= 0 && wishlistVisitable.getOrNull(recommendationPositionInPreviousPage) is WishlistV2TopAdsWrapper){
                        pageToken = (wishlistVisitable[recommendationPositionInPreviousPage] as WishlistV2TopAdsWrapper).topAdsData.nextPageToken ?: ""
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
                            recommendationPositionInPage= recommendationPositionInPage,
                            currentPage = currentPage,
                            maxItemInPage = maxItemInPage
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
            } catch (e: Throwable){
                return@withContext wishlistVisitable
            }
        }
    }

    companion object {
        private const val recommendationPositionInPage = 4
        private const val maxItemInPage = 21
        private const val newMinItemRegularRule = 24
        private const val WISHLIST_TOPADS_SOURCE = "6"
        private const val WISHLIST_TOPADS_ADS_COUNT = 1
        private const val WISHLIST_TOPADS_DIMENS = 3
        private const val WISHLIST_PAGE_NAME = "wishlist"

        private const val RECOM_INDEX_STARTER = 4
        private const val RECOM_INDEX_STARTER_MINUS = 3
    }
}