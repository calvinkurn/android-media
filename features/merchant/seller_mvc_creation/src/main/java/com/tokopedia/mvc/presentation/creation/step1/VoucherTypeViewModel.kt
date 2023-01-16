package com.tokopedia.mvc.presentation.creation.step1

import android.content.SharedPreferences
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneAction
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneEvent
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneUiState
import com.tokopedia.mvc.util.constant.CommonConstant
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class VoucherTypeViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val TRUE = 1
    }

    private val _uiState = MutableStateFlow(VoucherCreationStepOneUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<VoucherCreationStepOneAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    private val currentState: VoucherCreationStepOneUiState
        get() = _uiState.value

    fun processEvent(event: VoucherCreationStepOneEvent) {
        when (event) {
            is VoucherCreationStepOneEvent.InitVoucherConfiguration -> {
                initVoucherConfiguration(event.voucherConfiguration)
            }
            is VoucherCreationStepOneEvent.ChooseVoucherType -> {
                handleVoucherTypeSelection(event.pageMode, event.isVoucherProduct)
            }
            is VoucherCreationStepOneEvent.HandleCoachmark -> {
                handleCoachmark()
            }
            is VoucherCreationStepOneEvent.NavigateToNextStep -> {
                navigateToNextStep()
            }
        }
    }

    private fun initVoucherConfiguration(voucherConfiguration: VoucherConfiguration) {
        _uiState.update {
            it.copy(isLoading = false, voucherConfiguration = voucherConfiguration)
        }
    }

    private fun handleVoucherTypeSelection(
        pageMode: PageMode,
        isVoucherProduct: Boolean
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val action =
                    if (pageMode == PageMode.CREATE) VoucherAction.CREATE else VoucherAction.UPDATE

                val metadataParam = GetInitiateVoucherPageUseCase.Param(
                    action = action,
                    promoType = currentState.voucherConfiguration.promoType,
                    isVoucherProduct = isVoucherProduct
                )
                val metadata = getInitiateVoucherPageUseCase.execute(metadataParam)

                if (metadata.isEligible == TRUE) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            voucherConfiguration = it.voucherConfiguration.copy(
                                isVoucherProduct = isVoucherProduct,
                                voucherCodePrefix = metadata.prefixVoucherCode
                            )
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiAction.tryEmit(
                        VoucherCreationStepOneAction.ShowIneligibleState(
                            isVoucherProduct
                        )
                    )
                }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
                _uiAction.tryEmit(VoucherCreationStepOneAction.ShowError(error))
            }
        )
    }

    private fun navigateToNextStep() {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(isFinishFilledStepOne = true)
            )
        }
        _uiAction.tryEmit(
            VoucherCreationStepOneAction.NavigateToNextStep(
                currentState.pageMode,
                currentState.voucherConfiguration
            )
        )
    }

    private fun handleCoachmark() {
        if (!isCoachMarkShown()) {
            _uiAction.tryEmit(VoucherCreationStepOneAction.ShowCoachmark)
        }
    }

    private fun isCoachMarkShown(): Boolean {
        return sharedPreferences.getBoolean(
            CommonConstant.SHARED_PREF_VOUCHER_CREATION_STEP_ONE_COACH_MARK,
            false
        )
    }

    fun setSharedPrefCoachMarkAlreadyShown() {
        sharedPreferences.edit()
            .putBoolean(CommonConstant.SHARED_PREF_VOUCHER_CREATION_STEP_ONE_COACH_MARK, true)
            .apply()
    }
}
