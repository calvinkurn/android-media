package com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.item.PriceRangeFilterCheckboxItemUiModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class TokofoodQuickPriceRangeViewModel @Inject constructor(
    val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _initialOptions = MutableLiveData<List<Option>>(listOf())
    private val _currentAppliedOptions = MutableLiveData<List<Option>>(listOf())
    private val _currentUiModels = MutableLiveData<List<PriceRangeFilterCheckboxItemUiModel>>(listOf())

    private val _appliedOptions = MutableSharedFlow<List<Option>>(Int.ONE)

    private val _applyButtonClicked = MutableSharedFlow<List<Option>>(Int.ONE)
    val applyButtonClicked: SharedFlow<List<Option>>
        get() = _applyButtonClicked

    @FlowPreview
    val shouldShowApplyButton: SharedFlow<Boolean> =
        _appliedOptions.flatMapConcat { options ->
            flow {
                _currentAppliedOptions.value = options
                emit(!isOptionsSameAsInitial(options))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARING_DELAY_MILLIS),
            replay = Int.ONE
        )

    @FlowPreview
    val shouldShowResetButton: SharedFlow<Boolean> =
        _appliedOptions.flatMapConcat { options ->
            flow {
                val appliedCount = options.count { it.inputState.toBoolean() }
                emit(appliedCount > Int.ZERO)
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARING_DELAY_MILLIS),
            replay = Int.ONE
        )

    private val _currentUiModelsFlow = MutableSharedFlow<List<PriceRangeFilterCheckboxItemUiModel>>(Int.ONE)
    val currentUiModelsFlow: SharedFlow<List<PriceRangeFilterCheckboxItemUiModel>>
        get() = _currentUiModelsFlow

    fun resetUiModels() {
        val resetUiModels = _currentUiModels.value?.onEach {
            it.isSelected = false
        }.orEmpty()
        setPriceRangeUiModels(resetUiModels)
    }

    fun setPriceRangeUiModel(
        uiModel: PriceRangeFilterCheckboxItemUiModel,
        isSelected: Boolean
    ) {
        val appliedOptions = getCurrentAppliedOptions().toMutableList()
        val newAppliedOptions = appliedOptions.map {
            it.copy().apply {
                if (uiModel.option.value == it.value) {
                    inputState = isSelected.toString()
                }
            }
        }
        _appliedOptions.tryEmit(newAppliedOptions)
    }

    fun setPriceRangeUiModels(uiModels: List<PriceRangeFilterCheckboxItemUiModel>,
                              isInitialSet: Boolean = false) {
        val updatedOptions =
            uiModels.map {
                it.option.copy().apply {
                    inputState = it.isSelected.toString()
                }
            }
        _currentUiModels.value = uiModels
        _currentUiModelsFlow.tryEmit(uiModels)
        _appliedOptions.tryEmit(updatedOptions)
        if (isInitialSet) {
            _initialOptions.value = updatedOptions
        }
    }

    fun clickApplyButton() {
        _applyButtonClicked.tryEmit(getCurrentAppliedOptions())
    }

    private fun getCurrentAppliedOptions(): List<Option> =
        _currentAppliedOptions.value.orEmpty()

    private fun isOptionsSameAsInitial(options: List<Option>): Boolean {
        return isOptionsSizeSameAsInitial(options) && isSelectedOptionsSameAsInitial(options)
    }

    private fun isOptionsSizeSameAsInitial(options: List<Option>): Boolean {
        val appliedCount = options.count { it.inputState.toBoolean() }
        return appliedCount == getInitialAppliedCount()
    }

    private fun getInitialAppliedCount(): Int {
        return _initialOptions.value?.count { it.inputState.toBoolean() }.orZero()
    }

    private fun isSelectedOptionsSameAsInitial(options: List<Option>): Boolean {
        options.forEach { option ->
            if (_initialOptions.value?.any { it.value == option.value && it.inputState != option.inputState } == true) {
                return false
            }
        }
        return true
    }

    companion object {
        private const val SHARING_DELAY_MILLIS = 5000L
    }

}