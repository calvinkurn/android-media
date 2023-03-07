package com.tokopedia.deals.checkout.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common_entertainment.data.DealsCheckoutInstantResponse
import com.tokopedia.common_entertainment.data.DealsCheckoutResponse
import com.tokopedia.common_entertainment.data.DealsGeneral
import com.tokopedia.common_entertainment.data.DealsInstant
import com.tokopedia.deals.checkout.domain.DealsCheckoutGeneralV2InstantUseCase
import com.tokopedia.deals.checkout.domain.DealsCheckoutGeneralV2UseCase
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

class DealsCheckoutViewModel @Inject constructor(
    private val dealsCheckoutGeneralV2UseCase: DealsCheckoutGeneralV2UseCase,
    private val dealsCheckoutGeneralV2InstantUseCase: DealsCheckoutGeneralV2InstantUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _inputCheckoutGeneralV2 = MutableSharedFlow<DealsGeneral>(Int.ONE)
    private val _inputCheckoutGeneralV2Instant = MutableSharedFlow<DealsInstant>(Int.ONE)

    val flowCheckoutGeneral: SharedFlow<Result<DealsCheckoutResponse>> =
        _inputCheckoutGeneralV2.flatMapConcat {
            flow {
                emit(getCheckoutGeneral(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowCheckoutGeneralInstant: SharedFlow<Result<DealsCheckoutInstantResponse>> =
        _inputCheckoutGeneralV2Instant.flatMapConcat {
            flow {
                emit(getCheckoutGeneralInstant(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    fun checkoutGeneral(checkoutGeneralParam: DealsGeneral) {
        _inputCheckoutGeneralV2.tryEmit(checkoutGeneralParam)
    }

    fun checkoutGeneralInstant(checkoutGeneralInstantParam: DealsInstant) {
        _inputCheckoutGeneralV2Instant.tryEmit(checkoutGeneralInstantParam)
    }

    private suspend fun getCheckoutGeneral(checkoutGeneralParam: DealsGeneral): Result<DealsCheckoutResponse> {
        val checkoutGeneralResponse = withContext(dispatcher.io) {
            dealsCheckoutGeneralV2UseCase.execute(checkoutGeneralParam)
        }

        return Success(checkoutGeneralResponse)
    }

    private suspend fun getCheckoutGeneralInstant(checkoutGeneralInstantParam: DealsInstant): Result<DealsCheckoutInstantResponse> {
        val checkoutGeneralInstantResponse = withContext(dispatcher.io) {
            dealsCheckoutGeneralV2InstantUseCase.execute(checkoutGeneralInstantParam)
        }

        return Success(checkoutGeneralInstantResponse)
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
    }
}
