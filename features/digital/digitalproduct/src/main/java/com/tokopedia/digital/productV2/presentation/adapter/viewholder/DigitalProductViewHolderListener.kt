package com.tokopedia.digital.productV2.presentation.adapter.viewholder

import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.digital.productV2.presentation.model.DigitalProductSelectDropdownData

interface OnInputListener {
    fun onFinishInput(label: String, input: String, position: Int)
    fun onCustomInputClick(field: TopupBillsInputFieldWidget,
                           data: List<DigitalProductSelectDropdownData>,
                           position: Int)
}