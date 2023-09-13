package com.tokopedia.stories.widget.domain

import com.tokopedia.stories.widget.StoriesStatus

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
data class StoriesWidgetInfo(
    val widgetStates: Map<String, StoriesWidgetState>,
    val coachMarkText: String
) {
    companion object {
        val Default: StoriesWidgetInfo
            get() = StoriesWidgetInfo(
                widgetStates = emptyMap(),
                coachMarkText = ""
            )
    }
}

data class StoriesWidgetState(
    val shopId: String,
    val status: StoriesStatus,
    val appLink: String,
    val updatedAt: TimeMillis
) {
    companion object {
        val Default: StoriesWidgetState
            get() = StoriesWidgetState(
                shopId = "",
                status = StoriesStatus.NoStories,
                appLink = "",
                updatedAt = TimeMillis.now()
            )
    }
}

@JvmInline
value class TimeMillis private constructor(val time: Long) {

    companion object {
        fun now(): TimeMillis {
            return TimeMillis(System.currentTimeMillis())
        }
    }
}
