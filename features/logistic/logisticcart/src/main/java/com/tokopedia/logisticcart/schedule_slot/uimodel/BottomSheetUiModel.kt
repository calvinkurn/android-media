package com.tokopedia.logisticcart.schedule_slot.uimodel

data class BottomSheetUiModel(
    val date: ChooseDateUiModel,
    val availableTime: List<ChooseTimeUiModel>,
    val unavailableTime: List<ChooseTimeUiModel>
)
