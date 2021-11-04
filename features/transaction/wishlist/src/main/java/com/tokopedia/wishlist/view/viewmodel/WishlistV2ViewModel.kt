package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
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
class WishlistV2ViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                              private val wishlistV2UseCase: WishlistV2UseCase,
                                              private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
                                              private val recommendationUseCase: GetRecommendationUseCase,
                                              private val topAdsImageViewUseCase: TopAdsImageViewUseCase
) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2Result = MutableLiveData<Result<WishlistV2Data>>()
    val wishlistV2Result: LiveData<Result<WishlistV2Data>>
        get() = _wishlistV2Result

    private val _recommendationResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val recommendationResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _recommendationResult

    private val _deleteWishlistV2Result = MutableLiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>()
    val deleteWishlistV2Result: LiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _deleteWishlistV2Result


    fun loadWishlistV2(params: WishlistV2Params) {
        launch {
            try {
                val wishlistData = wishlistV2UseCase.executeSuspend(params)
                if (wishlistData.items.isEmpty()) {
                    // get recommendation on empty
                } else {
                    _wishlistV2Result.value = Success(wishlistData)
                    when {
                        wishlistData.items.size < recommendationPositionInPage -> {
                            _wishlistV2Result.value = Success(getRecomList(pageNumber = pageNumber))
                        }

                        // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                        wishlistData.items.size == recommendationPositionInPage -> {
                            // TODO top ads pagetoken
                            _wishlistV2Result.value = Success(getTopAds("TODO"))
                            _wishlistV2Result.value = Success(getRecomList(pageNumber = pageNumber))
                        }

                        // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
                        wishlistData.items.size > recommendationPositionInPage -> {
                            if (wishlistData.totalData > newMinItemRegularRule) {
                                _wishlistV2Result.value = getTopAdsBannerData(visitableWishlist, currentPage, data.items.map { it.id }, recommendationPositionInPage)
                            } else {
                                _wishlistV2Result.value = getTopadsAndRecommendationWishlist(visitableWishlist, currentPage, data.items.map { it.id }, data.items.size)
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
            _deleteWishlistV2Result.value = deleteWishlistV2UseCase.executeSuspend(productId, userId)
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
                _recommendationResult.value = Success(data)
            } catch (e: Exception) {
                _recommendationResult.value = Fail(e.fillInStackTrace())
            }
        }
    }

    private suspend fun getRecomList(pageNumber: Int) : WishlistV2RecommendationWrapper =
        withContext(dispatcher.io) {
            return@withContext WishlistV2RecommendationWrapper(recommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageNumber = pageNumber,
                    pageName = WishlistV2Consts.PAGE_NAME
                ))
            )
        }

    private suspend fun getTopAds(pageToken: String) : WishlistV2TopAdsWrapper =
        withContext(dispatcher.io) {
            return@withContext WishlistV2TopAdsWrapper(topAdsImageViewUseCase.getImageData(
                topAdsImageViewUseCase.getQueryMap(
                    "",
                    WISHLIST_TOPADS_SOURCE,
                    pageToken,
                    WISHLIST_TOPADS_ADS_COUNT,
                    WISHLIST_TOPADS_DIMENS,
                    ""
                )
            ).first())
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