package com.tokopedia.deals.ui.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.ui.pdp.domain.DealsPDPDetailUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPEventContentUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPGetRatingUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPRecentSearchTrackingUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPRecommendTrackingUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPRecommendationUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPUpdateRatingUseCase
import com.tokopedia.deals.ui.pdp.ui.utils.DealsPDPMapper
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

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
    private val _inputUpdateRatingState = MutableSharedFlow<com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateRequest>(Int.ONE)
    private val _inputTrackingRecommend = MutableSharedFlow<com.tokopedia.deals.ui.pdp.data.DealsRecommendTrackingRequest>(Int.ONE)
    private val _inputTrackingRecentSearch = MutableSharedFlow<com.tokopedia.deals.ui.pdp.data.DealsTravelRecentSearchTrackingRequest>(Int.ONE)

    val flowPDP: SharedFlow<Result<com.tokopedia.deals.ui.pdp.data.DealsProductDetail>> =
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

    val flowContent: SharedFlow<Result<com.tokopedia.deals.ui.pdp.data.DealsProductEventContent>> =
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

    val flowRating: SharedFlow<Result<com.tokopedia.deals.ui.pdp.data.DealsRatingResponse>> =
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

    val flowUpdateRating: SharedFlow<Result<com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse>> =
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

    val flowRecommendationTracking: SharedFlow<Result<com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse>> =
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

    val flowRecentSearchTracking: SharedFlow<Result<com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse>> =
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
        _inputUpdateRatingState.tryEmit(DealsPDPMapper.mapperParamUpdateRating(productId, userId, isLiked))
    }

    fun setTrackingRecommendation(productId: String, userId: String) {
        _inputTrackingRecommend.tryEmit(DealsPDPMapper.mapperParamTrackingRecommendation(productId, userId))
    }

    fun setTrackingRecentSearch(productDetailData: com.tokopedia.deals.ui.pdp.data.ProductDetailData, userId: String) {
        _inputTrackingRecentSearch.tryEmit(DealsPDPMapper.mapperParamTrackingRecentSearch(productDetailData, userId))
    }

    private suspend fun getPDP(productId: String): Result<com.tokopedia.deals.ui.pdp.data.DealsProductDetail> {
        val pdpDataResponse = withContext(dispatcher.io) {
            dealsPDPDetailUseCase.execute(productId)
        }

        return Success(pdpDataResponse)
    }

    private suspend fun getContent(productId: String): Result<com.tokopedia.deals.ui.pdp.data.DealsProductEventContent> {
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

    private suspend fun getRating(productId: String): Result<com.tokopedia.deals.ui.pdp.data.DealsRatingResponse> {
        dealsPDPGetRatingUseCase.setUrlId(productId)
        val dealsRatingResponse = withContext(dispatcher.io) {
            convertToRatingResponse(dealsPDPGetRatingUseCase.executeOnBackground())
        }

        return Success(dealsRatingResponse)
    }

    private suspend fun updateRating(dealsRatingUpdateRequest: com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateRequest): Result<com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse> {
        dealsPDPUpdateRatingUseCase.setParam(dealsRatingUpdateRequest)
        val dealsUpdateRating = withContext(dispatcher.io) {
            convertToUpdateRatingResponse(dealsPDPUpdateRatingUseCase.executeOnBackground())
        }

        return Success(dealsUpdateRating)
    }

    private suspend fun trackRecommendation(dealsRecommendTrackingRequest: com.tokopedia.deals.ui.pdp.data.DealsRecommendTrackingRequest): Result<com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse> {
        dealsPDPRecommendTrackingUseCase.setParam(dealsRecommendTrackingRequest)
        val dealsRecommendTrackingResponse = withContext(dispatcher.io) {
            convertToTrackingResponse(dealsPDPRecommendTrackingUseCase.executeOnBackground())
        }

        return Success(dealsRecommendTrackingResponse)
    }

    private suspend fun trackRecentSearch(dealsTravelRecentSearchTrackingRequest: com.tokopedia.deals.ui.pdp.data.DealsTravelRecentSearchTrackingRequest): Result<com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse> {
        dealsPDPRecentSearchTrackingUseCase.setParam(dealsTravelRecentSearchTrackingRequest)
        val dealsRecentSearchTrackingResponse = withContext(dispatcher.io) {
            convertToTrackingResponse(dealsPDPRecentSearchTrackingUseCase.executeOnBackground())
        }

        return Success(dealsRecentSearchTrackingResponse)
    }

    private fun convertToRatingResponse(typeRestResponseMap: Map<Type, RestResponse?>): com.tokopedia.deals.ui.pdp.data.DealsRatingResponse {
        return typeRestResponseMap[com.tokopedia.deals.ui.pdp.data.DealsRatingResponse::class.java]?.getData() as com.tokopedia.deals.ui.pdp.data.DealsRatingResponse
    }

    private fun convertToUpdateRatingResponse(typeRestResponseMap: Map<Type, RestResponse?>): com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse {
        return typeRestResponseMap[com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse::class.java]?.getData() as com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse
    }

    private fun convertToTrackingResponse(typeRestResponseMap: Map<Type, RestResponse?>): com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse {
        return typeRestResponseMap[com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse::class.java]?.getData() as com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        private const val TYPE_ID = "4"
    }
}
