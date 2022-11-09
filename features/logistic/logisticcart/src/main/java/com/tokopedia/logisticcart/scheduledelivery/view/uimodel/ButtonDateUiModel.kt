package com.tokopedia.logisticcart.scheduledelivery.view.uimodel

data class ButtonDateUiModel (
    val title: String = "",
    val date: String = "",
    val isEnabled: Boolean = true,
    val id: String = "",
    var isSelected: Boolean = false,
    val availableTime: List<ChooseTimeUiModel>,
    val unavailableTime: List<ChooseTimeUiModel>
)
