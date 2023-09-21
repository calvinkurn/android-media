package com.tokopedia.order_management_common.presentation.uimodel

data class ActionButtonsUiModel(
    val primaryActionButton: ActionButton,
    val secondaryActionButtons: List<ActionButton>
) {
    data class ActionButton(
        val key: String,
        val label: String,
        val popUp: PopUp,
        val variant: String,
        val type: String,
        val url: String
    ) {
        data class PopUp(
            val actionButton: List<PopUpButton> = listOf(),
            val body: String,
            val title: String
        ) {
            data class PopUpButton(
                val key: String,
                val displayName: String,
                val color: String,
                val type: String,
                val uriType: String,
                val uri: String
            )
        }
    }
}
