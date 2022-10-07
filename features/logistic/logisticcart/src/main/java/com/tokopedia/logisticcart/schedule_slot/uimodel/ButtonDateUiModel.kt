package com.tokopedia.logisticcart.schedule_slot.uimodel

data class ButtonDateUiModel (
    val title: String = "",
    val date: String = "",
    val isEnabled: Boolean = true,
    val id: String = "",
    val isSelected: Boolean = false,
    val availableTime: List<ChooseTimeUiModel>,
    val unavailableTime: List<ChooseTimeUiModel>
)
