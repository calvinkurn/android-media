package com.tokopedia.rechargegeneral.presentation.adapter.viewholder

import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductInput
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralProductSelectData

interface OnInputListener {
    fun onFinishInput(label: String, input: String, position: Int, isManual: Boolean = false)
    fun onTextChangeInput()
    fun onCustomInputClick(field: TopupBillsInputFieldWidget,
                           enquiryData: RechargeGeneralProductInput? = null,
                           productData: List<RechargeGeneralProductSelectData>? = null)
    fun onInfoClick(text: String)
}