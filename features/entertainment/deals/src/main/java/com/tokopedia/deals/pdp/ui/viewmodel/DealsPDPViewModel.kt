package com.tokopedia.deals.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.pdp.data.DealRatingRequest
import com.tokopedia.deals.pdp.data.DealsProductDetail
import com.tokopedia.deals.pdp.data.DealsProductEventContent
import com.tokopedia.deals.pdp.data.DealsRatingResponse
import com.tokopedia.deals.pdp.data.DealsRatingUpdateRequest
import com.tokopedia.deals.pdp.data.DealsRatingUpdateResponse
import com.tokopedia.deals.pdp.data.DealsRecommendMessage
import com.tokopedia.deals.pdp.data.DealsRecommendTrackingRequest
import com.tokopedia.deals.pdp.data.DealsTrackingResponse
import com.tokopedia.deals.pdp.data.DealsTravelMessage
import com.tokopedia.deals.pdp.data.DealsTravelRecentSearchTrackingRequest
import com.tokopedia.deals.pdp.data.Entertainment
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.data.RecentData
import com.tokopedia.deals.pdp.data.TravelRecentSearch
import com.tokopedia.deals.pdp.domain.DealsPDPDetailUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPEventContentUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPGetRatingUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPRecentSearchTrackingUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPRecommendTrackingUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPRecommendationUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPUpdateRatingUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.lang.reflect.Type
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

class DealsPDPViewModel @Inject constructor(
    private val dealsPDPDetailUseCase: DealsPDPDetailUseCase,
    private val dealsPDPEventContentUseCase: DealsPDPEventContentUseCase,
    private val dealsPDPRecommendationUseCase: DealsPDPRecommendationUseCase,
    private val dealsPDPGetRatingUseCase: DealsPDPGetRatingUseCase,
    private val dealsPDPUpdateRatingUseCase: DealsPDPUpdateRatingUseCase,
    private val dealsPDPRecommendTrackingUseCase: DealsPDPRecommendTrackingUseCase,
    private val dealsPDPRecentSearchTrackingUseCase: DealsPDPRecentSearchTrackingUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _inputPDPState = MutableSharedFlow<String>(Int.ONE)
    private val _inputContentState = MutableSharedFlow<String>(Int.ONE)
    private val _inputRecommendationState = MutableSharedFlow<String>(Int.ONE)
    private val _inputRatingState = MutableSharedFlow<String>(Int.ONE)
    private val _inputUpdateRatingState = MutableSharedFlow<DealsRatingUpdateRequest>(Int.ONE)
    private val _inputTrackingRecommend = MutableSharedFlow<DealsRecommendTrackingRequest>(Int.ONE)
    private val _inputTrackingRecentSearch = MutableSharedFlow<DealsTravelRecentSearchTrackingRequest>(Int.ONE)

    val flowPDP: SharedFlow<Result<DealsProductDetail>> =
        _inputPDPState.flatMapConcat {
            flow {
                emit(getPDP(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowContent: SharedFlow<Result<DealsProductEventContent>> =
        _inputContentState.flatMapConcat {
            flow {
                emit(getContent(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowRecommendation: SharedFlow<Result<SearchData>> =
        _inputRecommendationState.flatMapConcat {
            flow {
                emit(getRecommendation(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowRating: SharedFlow<Result<DealsRatingResponse>> =
        _inputRatingState.flatMapConcat {
            flow {
                emit(getRating(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowUpdateRating: SharedFlow<Result<DealsRatingUpdateResponse>> =
        _inputUpdateRatingState.flatMapConcat {
            flow {
                emit(updateRating(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowRecommendationTracking: SharedFlow<Result<DealsTrackingResponse>> =
        _inputTrackingRecommend.flatMapConcat {
            flow {
                emit(trackRecommendation(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowRecentSearchTracking: SharedFlow<Result<DealsTrackingResponse>> =
        _inputTrackingRecentSearch.flatMapConcat {
            flow {
                emit(trackRecentSearch(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    var isLiked = false
    var totalLikes = Int.ZERO

    fun setPDP(urlProduct: String) {
        _inputPDPState.tryEmit(urlProduct)
    }

    fun setContent(productId: String) {
        _inputContentState.tryEmit(productId)
    }

    fun setRecommendation(childCategoryId: String) {
        _inputRecommendationState.tryEmit(childCategoryId)
    }

    fun setRating(productId: String) {
        _inputRatingState.tryEmit(productId)
    }

    fun updateRating(productId: String, userId: String, isLiked: Boolean) {
        _inputUpdateRatingState.tryEmit(mapperParamUpdateRating(productId, userId, isLiked))
    }

    fun setTrackingRecommendation(productId: String, userId: String) {
        _inputTrackingRecommend.tryEmit(mapperParamTrackingRecommendation(productId, userId))
    }

    fun setTrackingRecentSearch(productDetailData: ProductDetailData, userId: String) {
        _inputTrackingRecentSearch.tryEmit(mapperParamTrackingRecentSearch(productDetailData, userId))
    }

    fun productImagesMapper(productDetail: ProductDetailData): List<String> {
        val images = mutableListOf<String>()
        if (productDetail.media.isNotEmpty()) {
            images.addAll(
                productDetail.media.map {
                    it.url
                }
            )
        } else {
            images.add(productDetail.imageApp)
        }
        return images
    }

    private suspend fun getPDP(productId: String): Result<DealsProductDetail> {
        val pdpDataResponse = withContext(dispatcher.io) {
            dealsPDPDetailUseCase.execute(productId)
        }

        return Success(pdpDataResponse)
    }

    private suspend fun getContent(productId: String): Result<DealsProductEventContent> {
        val eventContentResponse = withContext(dispatcher.io) {
            dealsPDPEventContentUseCase.execute(TYPE_ID, productId)
        }

        return Success(eventContentResponse)
    }

    private suspend fun getRecommendation(childCategoryId: String): Result<SearchData> {
        val dealsPDPRecommendationResponse = withContext(dispatcher.io) {
            dealsPDPRecommendationUseCase.execute(childCategoryId)
        }

        return Success(dealsPDPRecommendationResponse)
    }

    private suspend fun getRating(productId: String): Result<DealsRatingResponse> {
        dealsPDPGetRatingUseCase.setUrlId(productId)
        val dealsRatingResponse = withContext(dispatcher.io) {
            convertToRatingResponse(dealsPDPGetRatingUseCase.executeOnBackground())
        }

        return Success(dealsRatingResponse)
    }

    private suspend fun updateRating(dealsRatingUpdateRequest: DealsRatingUpdateRequest): Result<DealsRatingUpdateResponse> {
        dealsPDPUpdateRatingUseCase.setParam(dealsRatingUpdateRequest)
        val dealsUpdateRating = withContext(dispatcher.io) {
            convertToUpdateRatingResponse(dealsPDPUpdateRatingUseCase.executeOnBackground())
        }

        return Success(dealsUpdateRating)
    }

    private suspend fun trackRecommendation(dealsRecommendTrackingRequest: DealsRecommendTrackingRequest): Result<DealsTrackingResponse> {
        dealsPDPRecommendTrackingUseCase.setParam(dealsRecommendTrackingRequest)
        val dealsRecommendTrackingResponse = withContext(dispatcher.io) {
            convertToTrackingResponse(dealsPDPRecommendTrackingUseCase.executeOnBackground())
        }

        return Success(dealsRecommendTrackingResponse)
    }

    private suspend fun trackRecentSearch(dealsTravelRecentSearchTrackingRequest: DealsTravelRecentSearchTrackingRequest): Result<DealsTrackingResponse> {
        dealsPDPRecentSearchTrackingUseCase.setParam(dealsTravelRecentSearchTrackingRequest)
        val dealsRecentSearchTrackingResponse = withContext(dispatcher.io) {
            convertToTrackingResponse(dealsPDPRecentSearchTrackingUseCase.executeOnBackground())
        }

        return Success(dealsRecentSearchTrackingResponse)
    }

    private fun mapperParamUpdateRating(productId: String, userId: String, isLiked: Boolean): DealsRatingUpdateRequest {
        return DealsRatingUpdateRequest(
            DealRatingRequest(
                feedback = "",
                isLiked = isLiked.toString(),
                productId = productId.toIntSafely().toLong(),
                rating = Int.ZERO,
                userId = userId.toIntSafely().toLong()
            )
        )
    }

    private fun mapperParamTrackingRecommendation(productId: String, userId: String): DealsRecommendTrackingRequest {
        return DealsRecommendTrackingRequest(
            message = DealsRecommendMessage(
                action = ACTION_TRACKING_RECOMMENDATION,
                productId = productId.toIntSafely().toLong(),
                useCase = NSQ_USE_CASE,
                userId = userId.toIntSafely().toLong()
            ),
            service = SERVICE_TRACKING_RECOMMENDATION
        )
    }

    private fun mapperParamTrackingRecentSearch(productDetailData: ProductDetailData, userId: String): DealsTravelRecentSearchTrackingRequest {
        return DealsTravelRecentSearchTrackingRequest(
            message = DealsTravelMessage(
                userId = userId.toIntSafely().toLong()
            ),
            service = SERVICE_TRACKING_RECENT_SEARCH,
            travelRecentSearch = TravelRecentSearch(
                dataType = DEALS_DATA_TYPE,
                recentData = RecentData(
                    entertainment = Entertainment(
                        appUrl = productDetailData.appUrl,
                        value = productDetailData.displayName,
                        price = DealsUtils.convertToCurrencyString(productDetailData.salesPrice.toIntSafely().toLong()),
                        imageUrl = if (productDetailData.media.isNotEmpty()) productDetailData.media.first().url else "",
                        id = productDetailData.brand.title,
                        pricePrefix = DealsUtils.convertToCurrencyString(productDetailData.mrp.toIntSafely().toLong()),
                        url = productDetailData.webUrl
                    )
                )
            )
        )
    }

    private fun convertToRatingResponse(typeRestResponseMap: Map<Type, RestResponse?>): DealsRatingResponse {
        return typeRestResponseMap[DealsRatingResponse::class.java]?.getData() as DealsRatingResponse
    }

    private fun convertToUpdateRatingResponse(typeRestResponseMap: Map<Type, RestResponse?>): DealsRatingUpdateResponse {
        return typeRestResponseMap[DealsRatingUpdateResponse::class.java]?.getData() as DealsRatingUpdateResponse
    }

    private fun convertToTrackingResponse(typeRestResponseMap: Map<Type, RestResponse?>): DealsTrackingResponse {
        return typeRestResponseMap[DealsTrackingResponse::class.java]?.getData() as DealsTrackingResponse
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        private const val TYPE_ID = "4"
        private const val ACTION_TRACKING_RECOMMENDATION = "product-detail"
        private const val SERVICE_TRACKING_RECOMMENDATION = "Recommendation_For_You"
        private const val SERVICE_TRACKING_RECENT_SEARCH = "travel_recent_search"
        private const val NSQ_USE_CASE = "24"
        private const val DEALS_DATA_TYPE = "deal"
    }
}
