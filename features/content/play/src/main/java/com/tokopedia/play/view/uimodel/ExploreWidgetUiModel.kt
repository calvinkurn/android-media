package com.tokopedia.play.view.uimodel


/**
 * @author by astidhiyaa on 28/11/22
 */
data class ExploreWidgetUiModel(
    val id: String,
) {
    companion object {
        val Empty: ExploreWidgetUiModel
            get() = ExploreWidgetUiModel(
                id = "",
            )
    }
}
