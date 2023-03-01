package com.tokopedia.search.result.mps

import androidx.lifecycle.ViewModel
import com.tokopedia.discovery.common.constants.SearchConstant.MPS.MPS_USE_CASE
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.utils.ChooseAddressWrapper
import com.tokopedia.search.utils.mvvm.SearchViewModel
import com.tokopedia.search.utils.toSearchParams
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
    private val mpsUseCase: UseCase<MPSModel>,
    private val chooseAddressWrapper: ChooseAddressWrapper,
): ViewModel(), SearchViewModel<MPSState> {

    private val _stateFlow = MutableStateFlow(mpsState)

    override val stateFlow: StateFlow<MPSState>
        get() = _stateFlow.asStateFlow()

    private val chooseAddressModel
        get() = stateFlow.value.chooseAddressModel

    fun onViewCreated() {
        reloadData()
    }

    private fun reloadData() {
        updateChooseAddress()
        multiProductSearch()
    }

    private fun updateChooseAddress() {
        _stateFlow.update { it.chooseAddress(chooseAddressWrapper.getChooseAddressData()) }
    }

    private fun multiProductSearch() {
        mpsUseCase.execute(
            onSuccess = ::onMPSSuccess,
            onError = ::onMPSFailed,
            useCaseRequestParams = mpsUseCaseRequestParams(),
        )
    }

    private fun mpsUseCaseRequestParams(): RequestParams = RequestParams.create().apply {
        putAll(chooseAddressParams())
    }

    private fun chooseAddressParams() =
        chooseAddressModel?.toSearchParams() ?: mapOf()

    private fun onMPSSuccess(mpsModel: MPSModel) {
        _stateFlow.update { it.success(mpsModel) }
    }

    private fun onMPSFailed(throwable: Throwable) {
        _stateFlow.update { it.error(throwable) }
    }

    fun onLocalizingAddressSelected() {
        reloadData()
    }

    fun onViewResumed() {
        if (isChooseAddressUpdated())
            reloadData()
    }

    private fun isChooseAddressUpdated(): Boolean {
        val chooseAddressModel = chooseAddressModel ?: return false

        return chooseAddressWrapper.isChooseAddressUpdated(chooseAddressModel)
    }
}
