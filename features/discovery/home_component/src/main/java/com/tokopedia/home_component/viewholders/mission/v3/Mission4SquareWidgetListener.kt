package com.tokopedia.home_component.viewholders.mission.v3

import com.tokopedia.home_component.visitable.Mission4SquareUiModel

interface Mission4SquareWidgetListener {
    fun onMissionClicked(model: Mission4SquareUiModel, position: Int)
    fun onMissionImpressed(model: Mission4SquareUiModel, position: Int)
}
