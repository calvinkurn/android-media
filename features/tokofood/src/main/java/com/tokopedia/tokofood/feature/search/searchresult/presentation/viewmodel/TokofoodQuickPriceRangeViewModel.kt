package com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.item.PriceRangeFilterCheckboxItemUiModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
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
                val appliedCount = options.count { it.inputState == true.toString() }
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
        val newAppliedOptions = getCurrentAppliedOptions().onEach {
            if (uiModel.option.value == it.value) {
                it.inputState = isSelected.toString()
            }
        }
        _appliedOptions.tryEmit(newAppliedOptions)
    }

    fun setPriceRangeUiModels(uiModels: List<PriceRangeFilterCheckboxItemUiModel>) {
        val updatedOptions =
            uiModels.map {
                it.option.apply {
                    inputState = it.isSelected.toString()
                }
            }
        _currentUiModels.value = uiModels
        _currentUiModelsFlow.tryEmit(uiModels)
        _appliedOptions.tryEmit(updatedOptions)
    }

    fun clickApplyButton() {
        _applyButtonClicked.tryEmit(getCurrentAppliedOptions())
    }

    private fun getCurrentAppliedOptions(): List<Option> =
        _currentAppliedOptions.value.orEmpty()

    companion object {
        private const val SHARING_DELAY_MILLIS = 5000L
    }

}