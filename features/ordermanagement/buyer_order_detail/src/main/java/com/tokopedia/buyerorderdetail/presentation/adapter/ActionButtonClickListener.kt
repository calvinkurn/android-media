package com.tokopedia.buyerorderdetail.presentation.adapter

import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel

interface ActionButtonClickListener {
    fun onActionButtonClicked(button: ActionButtonsUiModel.ActionButton)
}