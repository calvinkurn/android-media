package com.tokopedia.stories.widget

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
data class StoriesWidgetState(
    val shopId: String,
    val status: StoriesStatus,
    val appLink: String,
    val updatedAt: TimeMillis,
) {
    companion object {
        val Default: StoriesWidgetState
            get() = StoriesWidgetState(
                shopId = "",
                status = StoriesStatus.NoStories,
                appLink = "",
                updatedAt = TimeMillis.now(),
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
