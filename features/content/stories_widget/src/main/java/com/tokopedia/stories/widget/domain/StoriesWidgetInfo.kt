package com.tokopedia.stories.widget.domain

/**
 * Created by kenny.hadisaputra on 12/09/23
 */
data class StoriesWidgetInfo(
    val widgetStates: List<StoriesWidgetState>,
    val coachMarkText: String
) {
    companion object {
        val Default: StoriesWidgetInfo
            get() = StoriesWidgetInfo(
                widgetStates = emptyList(),
                coachMarkText = ""
            )
    }
}
