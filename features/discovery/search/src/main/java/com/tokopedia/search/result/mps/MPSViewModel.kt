package com.tokopedia.search.result.mps

import androidx.lifecycle.ViewModel
import com.tokopedia.discovery.common.constants.SearchConstant.MPS.MPS_USE_CASE
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.utils.mvvm.SearchViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

class MPSViewModel @Inject constructor(
    mpsState: MPSState = MPSState(),
    @param:Named(MPS_USE_CASE)
    private val mpsUseCase: UseCase<MPSModel>
): ViewModel(), SearchViewModel<MPSState> {

    private val _stateFlow = MutableStateFlow(mpsState)

    override val stateFlow: StateFlow<MPSState>
        get() = _stateFlow.asStateFlow()

    fun onViewCreated() {
        mpsUseCase.execute(
            onSuccess = ::onMPSSuccess,
            onError = ::onMPSFailed,
            useCaseRequestParams = RequestParams.create(),
        )
    }

    private fun onMPSSuccess(mpsModel: MPSModel) {
        _stateFlow.update { it.success(mpsModel) }
    }

    private fun onMPSFailed(throwable: Throwable) {
        _stateFlow.update { it.error(throwable) }
    }
}
