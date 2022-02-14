package com.tokopedia.addongifting.addonbottomsheet.view

import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel

interface AddOnActionListener {

    fun onCheckBoxCheckedChanged(addOnUiModel: AddOnUiModel)

    fun onAddOnImageClicked(addOnUiModel: AddOnUiModel)
}