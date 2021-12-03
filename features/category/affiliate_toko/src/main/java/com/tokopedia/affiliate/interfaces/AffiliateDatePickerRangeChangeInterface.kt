package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData

interface AffiliateDatePickerRangeChangeInterface {
    fun rangeChanged(range : AffiliateDatePickerData)
    fun onRangeSelectionButtonClicked()
}