package com.tokopedia.deals.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.pdp.data.DealsProductDetail
import com.tokopedia.deals.pdp.domain.DealsPDPDetailUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
    private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _inputPDPState = MutableSharedFlow<String>(Int.ONE)

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

    fun setPDP(productId: String) {
        _inputPDPState.tryEmit(productId)
    }

    private suspend fun getPDP(productId: String): Result<DealsProductDetail> {
        val pdpDataResponse = withContext(dispatcher.io) {
            dealsPDPDetailUseCase.execute(productId)
        }

        return Success(pdpDataResponse)
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
    }
}