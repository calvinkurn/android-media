package com.tokopedia.mvcwidget.views.benefit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.mvcwidget.data.mapper.toUiModel
import com.tokopedia.mvcwidget.usecases.GetPromoBenefitBottomSheetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PromoBenefitViewModel @Inject constructor(
    private val getPromoBenefit: GetPromoBenefitBottomSheetUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<BenefitUiModel> = MutableStateFlow(BenefitUiModel())
    val state: StateFlow<BenefitUiModel> = _state

    fun setId(id: String) {
        viewModelScope.launch {
            val result = getPromoBenefit(id)
            _state.value = result.toUiModel()
        }
    }
}
