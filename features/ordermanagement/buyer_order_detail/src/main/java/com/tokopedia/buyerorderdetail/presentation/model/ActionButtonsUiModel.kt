package com.tokopedia.buyerorderdetail.presentation.model

data class ActionButtonsUiModel(
        val primaryActionButton: ActionButton,
        val secondaryActionButtons: List<ActionButton>
) {
    data class ActionButton(
            val key: String,
            val label: String,
            val popUp: PopUp,
            val style: String,
            val url: String
    ) {
        data class PopUp(
                val actionButton: List<ActionButton> = listOf(),
                val body: String = "",
                val title: String = ""
        )
    }
}