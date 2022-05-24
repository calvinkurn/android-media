package com.tokopedia.tokofood.feature.merchant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import javax.inject.Inject

class OrderCustomizationViewModel @Inject constructor(
) : ViewModel() {

    fun isCustomOrderContainError(addOnUiModels: List<AddOnUiModel>): Boolean {
        return addOnUiModels.firstOrNull { it.isError } != null
    }
}