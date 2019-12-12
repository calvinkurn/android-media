package com.tokopedia.rechargegeneral.presentation.adapter.viewholder

import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralProductSelectDropdownData

interface OnInputListener {
    fun onFinishInput(label: String, input: String, position: Int)
    fun onCustomInputClick(field: TopupBillsInputFieldWidget,
                           data: List<RechargeGeneralProductSelectDropdownData>,
                           position: Int)
}