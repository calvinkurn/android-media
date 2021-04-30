package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class ActionButtonsUiModel(
        val primaryActionButton: ActionButton,
        val secondaryActionButtons: List<ActionButton>
) : Visitable<BuyerOrderDetailTypeFactory> {

    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    data class ActionButton(
            val key: String,
            val label: String,
            val popUp: PopUp,
            val style: String
    ) {
        data class PopUp(
                val actionButton: List<ActionButton> = listOf(),
                val body: String = "",
                val title: String = ""
        )
    }
}