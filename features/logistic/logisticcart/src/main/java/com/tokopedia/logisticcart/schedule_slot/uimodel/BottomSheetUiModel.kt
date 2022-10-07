package com.tokopedia.logisticcart.schedule_slot.uimodel

data class BottomSheetUiModel(
    val date: ChooseDateUiModel,
    val availableTitle: TitleSectionUiModel?,
    val unavailableTitle: TitleSectionUiModel?
)
