package com.tokopedia.buyerorderdetail.presentation.adapter.listener

import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel

interface ActionButtonClickListener {
    fun onActionButtonClicked(isFromPrimaryButton: Boolean, button: ActionButtonsUiModel.ActionButton)
}
