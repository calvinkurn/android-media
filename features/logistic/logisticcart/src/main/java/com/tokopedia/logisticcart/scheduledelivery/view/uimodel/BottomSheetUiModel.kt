package com.tokopedia.logisticcart.scheduledelivery.view.uimodel

data class BottomSheetUiModel(
    val date: ChooseDateUiModel,
    val availableTitle: TitleSectionUiModel,
    val unavailableTitle: TitleSectionUiModel,
    val infoUiModel: BottomSheetInfoUiModel,
)
