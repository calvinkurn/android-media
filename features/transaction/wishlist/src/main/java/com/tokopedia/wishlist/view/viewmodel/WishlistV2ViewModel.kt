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
import com.tokopedia.wishlist.ext.*
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WishlistV2ViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                            private val wishlistV2UseCase: WishlistV2UseCase,
                                            private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
                                            private val bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase,
                                            private val recommendationUseCase: GetRecommendationUseCase,
                                            private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
                                            private val singleRecommendationUseCase: GetSingleRecommendationUseCase,
                                            private val atcUseCase: AddToCartUseCase) : BaseViewModel(dispatcher.main) {

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
                    _wishlistV2Result.value = Success(
                        organizeWishlistV2Data(
                            visitableWishlist,
                            params.page,
                            wishlistData.items.map { it.id },
                            hasNextPage = wishlistData.hasNextPage,
                            totalData = wishlistData.totalData
                        )
                    )
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

    fun getNextPageWishlistData(param: WishlistV2Params) {
        launch {
            try {
                val data = wishlistV2UseCase.executeSuspend(param).wishlistV2

                val previousData = if (_wishlistV2Result.value is Success) {
                    (_wishlistV2Result.value as Success<List<WishlistV2Data>>).data.toMutableList()
                } else {
                    mutableListOf()
                }

                val newPageVisitableData =
                    previousData.combineVisitable(data.items.mappingWishlistToVisitable())

                _wishlistV2Result.value = Success(
                    organizeWishlistV2Data(
                        newPageVisitableData,
                        param.page,
                        data.items.map { it.id },
                        hasNextPage = data.hasNextPage,
                        totalData = data.totalData)
                )

            } catch (e: Exception) {
                _wishlistV2Result.value = Fail(e.fillInStackTrace())
            }
        }
    }

    private suspend fun organizeWishlistV2Data(
        wishlistVisitable: MutableList<WishlistV2Data>,
        page: Int,
        productIds: List<String>,
        hasNextPage: Boolean,
        totalData: Int
    ): List<WishlistV2Data> {
        return withContext(dispatcher.io) {
//            if user has 0-3 products, recom widget is at the bottom of the page (vertical/infinite scroll)
            if (wishlistVisitable.size < maxItemInPage) {
                val recommendationResult = getRecommendationData(
                    page = page,
                    productIds = productIds
                )
                wishlistVisitable.add(
                    recommendationResult
                )
            }
            else {
//                If user has >24 products → follow normal rules, banner ads is after 4th products, recom widget after 24th product
                if (wishlistVisitable.size >= topAdsPositionInPage) {
                    if (wishlistVisitable.size == topAdsPositionInPage) {
                        wishlistVisitable.add(getTopAdsData(""))
                    } else {
                        if (wishlistVisitable[topAdsPositionInPage] !is WishlistV2TopAdsDataModel) {
                            wishlistVisitable.add(topAdsPositionInPage, getTopAdsData(""))
                        }
                    }
                }
                if (wishlistVisitable.size >= recommendationPositionInPage) {
                    if (wishlistVisitable.size == recommendationPositionInPage) {
                        wishlistVisitable.add(
                            getRecommendationData(page, productIds)
                        )
                    } else {
                        if (wishlistVisitable[recommendationPositionInPage] !is WishlistV2RecommendationDataModel) {
                            wishlistVisitable.add(
                                recommendationPositionInPage,
                                getRecommendationData(page, productIds)
                            )
                        }

                    }
                }
            }
//            if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
            val lastRecommendationData = checkIfWishlistLessThanMinimalItem(
                hasNextPage = hasNextPage,
                page = page,
                productIds = productIds,
                totalData = totalData
            )
            if (lastRecommendationData != null) {
                wishlistVisitable.add(lastRecommendationData)
            }
            wishlistVisitable
        }
    }

    fun onCheckedBulkDeleteWishlist(productId: String, isChecked: Boolean) {
        val previousData = if (_wishlistV2Result.value is Success) {
            (_wishlistV2Result.value as Success<List<WishlistV2Data>>).data.toMutableList()
        } else {
            mutableListOf()
        }
        val selectedProductIndex = previousData.indexOfFirst { element -> element is WishlistV2DataModel && element.item.id == productId }
        if (selectedProductIndex != -1) {
            (previousData[selectedProductIndex] as WishlistV2DataModel).isChecked = isChecked
            _wishlistV2Result.value = Success(previousData)
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

    private suspend fun getRecommendationData(
        page: Int,
        productIds: List<String>
    ): WishlistV2RecommendationDataModel = withContext(dispatcher.io) {
        return@withContext WishlistV2RecommendationDataModel(
            recommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageNumber = page,
                    productIds = productIds,
                    pageName = WISHLIST_PAGE_NAME
                )
            ), true
        )
    }

    private suspend fun getTopAdsData(
        pageToken: String = ""
    ): WishlistV2TopAdsDataModel = withContext(dispatcher.io) {
        return@withContext WishlistV2TopAdsDataModel(
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

    private suspend fun checkIfWishlistLessThanMinimalItem(
        totalData: Int,
        hasNextPage: Boolean,
        page: Int,
        productIds: List<String>
    ): WishlistV2RecommendationDataModel? = withContext(dispatcher.io) {
        if ((totalData in (maxItemInPage + 1) until newMinItemRegularRule) && !hasNextPage) {
            return@withContext getRecommendationData(page, productIds)
        } else return@withContext null
    }

    companion object {
        private const val recommendationPositionInPage = 25
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