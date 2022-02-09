package com.tokopedia.addongifting.view

import com.tokopedia.addongifting.view.uimodel.AddOnUiModel

interface AddOnActionListener {

    fun onCheckBoxCheckedChanged(addOnUiModel: AddOnUiModel)

    fun onAddOnImageClicked(addOnUiModel: AddOnUiModel)
}