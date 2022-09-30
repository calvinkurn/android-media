package com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodQuickPriceRangeHelper
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeFilterCheckboxItemUiModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class TokofoodQuickPriceRangeViewModel @Inject constructor(
    private val helper: TokofoodQuickPriceRangeHelper,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _initialOptions = MutableLiveData<List<Option>>(null)
    private val _currentAppliedOptions = MutableLiveData<List<Option>>(null)
    private val _currentUiModels = MutableLiveData<List<PriceRangeFilterCheckboxItemUiModel>>(null)

    private val _appliedOptions = MutableSharedFlow<List<Option>>(Int.ONE)

    private val _applyButtonClicked = MutableSharedFlow<List<Option>>(Int.ONE)
    val applyButtonClicked: SharedFlow<List<Option>>
        get() = _applyButtonClicked

    @FlowPreview
    val shouldShowApplyButton: SharedFlow<Boolean> =
        _appliedOptions.flatMapConcat { options ->
            flow {
                emit(!helper.getIsOptionsSameAsInitial(options, _initialOptions.value))
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
                val appliedCount = helper.getAppliedCount(options)
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
            it.clone().apply {
                if (uiModel.option.value == it.value) {
                    inputState = isSelected.toString()
                }
            }
        }
        setCurrentAppliedOptions(newAppliedOptions)
    }

    fun setPriceRangeUiModels(uiModels: List<PriceRangeFilterCheckboxItemUiModel>,
                              isInitialSet: Boolean = false) {
        val updatedOptions =
            uiModels.map {
                it.option.clone().apply {
                    inputState = it.isSelected.toString()
                }
            }
        _currentUiModels.value = uiModels
        _currentUiModelsFlow.tryEmit(uiModels)
        setCurrentAppliedOptions(updatedOptions)
        if (isInitialSet) {
            _initialOptions.value = updatedOptions
        }
    }

    fun clickApplyButton() {
        _applyButtonClicked.tryEmit(getCurrentAppliedOptions())
    }

    private fun setCurrentAppliedOptions(appliedOptions: List<Option>) {
        _currentAppliedOptions.value = appliedOptions
        _appliedOptions.tryEmit(appliedOptions)
    }

    private fun getCurrentAppliedOptions(): List<Option> =
        _currentAppliedOptions.value.orEmpty()

    companion object {
        private const val SHARING_DELAY_MILLIS = 5000L
    }

}