package com.tokopedia.deals.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common_entertainment.data.DealsVerifyRequest
import com.tokopedia.common_entertainment.data.DealsVerifyResponse
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.domain.DealsPDPVerifyUseCase
import com.tokopedia.deals.pdp.ui.utils.DealsPDPMapper
import com.tokopedia.kotlin.extensions.view.ONE
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
import javax.inject.Inject

class DealsPDPSelectQuantityViewModel @Inject constructor(
    private val dealsPDPVerifyUseCase: DealsPDPVerifyUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    var currentQuantity: Int = Int.ONE
    private val _inputVerifyState = MutableSharedFlow<DealsVerifyRequest>(Int.ONE)

    val flowVerify: SharedFlow<Result<DealsVerifyResponse>> =
        _inputVerifyState.flatMapConcat {
            flow {
                emit(verifyCheckout(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    fun setVerifyRequest(productDetailData: ProductDetailData) {
        _inputVerifyState.tryEmit(DealsPDPMapper.mapVerifyRequest(currentQuantity, productDetailData))
    }

    private suspend fun verifyCheckout(dealsVerifyRequest: DealsVerifyRequest): Result<DealsVerifyResponse> {
        val dealsVerifyResponse = withContext(dispatcher.io) {
            dealsPDPVerifyUseCase.execute(dealsVerifyRequest)
        }

        return Success(dealsVerifyResponse)
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
    }
}
