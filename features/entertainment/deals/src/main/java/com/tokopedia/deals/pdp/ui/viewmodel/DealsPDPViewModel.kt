package com.tokopedia.deals.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.pdp.data.DealsProductDetail
import com.tokopedia.deals.pdp.data.DealsProductEventContent
import com.tokopedia.deals.pdp.data.DealsRatingResponse
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.domain.DealsPDPDetailUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPEventContentUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPGetRatingUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPRecommendationUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.lang.reflect.Type
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
@FlowPreview
class DealsPDPViewModel @Inject constructor (
    private val dealsPDPDetailUseCase: DealsPDPDetailUseCase,
    private val dealsPDPEventContentUseCase: DealsPDPEventContentUseCase,
    private val dealsPDPRecommendationUseCase: DealsPDPRecommendationUseCase,
    private val dealsPDPGetRatingUseCase: DealsPDPGetRatingUseCase,
    private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _inputPDPState = MutableSharedFlow<String>(Int.ONE)
    private val _inputContentState = MutableSharedFlow<String>(Int.ONE)
    private val _inputRecommendationState = MutableSharedFlow<String>(Int.ONE)
    private val _inputRatingState = MutableSharedFlow<String>(Int.ONE)

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

    fun productImagesMapper(productDetail: ProductDetailData): List<String> {
        val images = mutableListOf<String>()
        if (productDetail.media.size > 0) {
            images.addAll(productDetail.media.map {
                it.url
            })
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
        val dealsRatingResponse = withContext(dispatcher.io){
            convertToValidateResponse(dealsPDPGetRatingUseCase.executeOnBackground())
        }

        return Success(dealsRatingResponse)
    }

    private fun convertToValidateResponse(typeRestResponseMap: Map<Type, RestResponse>): DealsRatingResponse {
        return typeRestResponseMap[DealsRatingResponse::class.java]?.getData() as DealsRatingResponse
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        private const val TYPE_ID = "4"
    }
}