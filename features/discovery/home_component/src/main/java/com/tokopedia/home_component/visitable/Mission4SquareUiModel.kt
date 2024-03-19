package com.tokopedia.home_component.visitable

data class Mission4SquareUiModel(
    val data: MissionWidgetDataModel,
    val title: Pair<String, TextStyle?>,
    val subtitle: Pair<String, TextStyle?>,
) {

    data class TextStyle(
        val isBold: Boolean = false,
        val textColor: String = ""
    )
}
