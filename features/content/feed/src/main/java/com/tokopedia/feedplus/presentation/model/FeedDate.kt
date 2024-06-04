package com.tokopedia.feedplus.presentation.model

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter

/**
 * Created by Jonathan Darwin on 26 April 2024
 */
@JvmInline
value class FeedDate(val date: String) {

    /**
     * @param date should be in format yyyy-MM-dd
     */

    val year: Int
        get() = unpackDate().getOrNull(0).toIntOrZero()

    val month: Int
        get() = unpackDate().getOrNull(1).toIntOrZero()

    val day: Int
        get() = unpackDate().getOrNull(2).toIntOrZero()

    val isEmpty: Boolean
        get() = date.isEmpty()

    private fun unpackDate(): List<String> {
        return date.split("-")
    }

    companion object {
        fun getCurrentDate(): FeedDate {
            val today = PlayDateTimeFormatter.formatDate(PlayDateTimeFormatter.getToday(), PlayDateTimeFormatter.yyyyMMdd)
            return FeedDate(today.orEmpty())
        }
    }
}
