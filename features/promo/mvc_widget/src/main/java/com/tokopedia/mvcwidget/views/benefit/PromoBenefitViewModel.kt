package com.tokopedia.mvcwidget.views.benefit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.mvcwidget.data.mapper.toUiModel
import com.tokopedia.mvcwidget.usecases.GetPromoBenefitBottomSheetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.lang.Thread.State
import javax.inject.Inject

class PromoBenefitViewModel @Inject constructor(
    private val getPromoBenefit: GetPromoBenefitBottomSheetUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<BenefitUiModel> = MutableStateFlow(BenefitUiModel())
    val state: StateFlow<BenefitUiModel> = _state

    private val _error: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    fun setId(metaData: String) {
        viewModelScope.launch {
            try {
                _error.value = false
                val result = getPromoBenefit(metaData)
                _state.value = result.toUiModel()
            } catch (e: Exception) {
                Timber.e(e)
                _error.value = true
            }
        }
    }
}
