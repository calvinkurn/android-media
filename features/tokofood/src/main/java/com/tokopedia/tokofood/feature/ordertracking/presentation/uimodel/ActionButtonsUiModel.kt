package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

data class ActionButtonsUiModel(
    val primaryActionButton: ActionButton,
    val secondaryActionButton: List<ActionButton>
) {
    data class ActionButton(
        val label: String,
        val appUrl: String,
        val type: String
    )
}
