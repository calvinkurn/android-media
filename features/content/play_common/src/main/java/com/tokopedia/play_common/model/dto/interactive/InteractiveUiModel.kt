package com.tokopedia.play_common.model.dto.interactive

import java.util.*

/**
 * Created by kenny.hadisaputra on 12/04/22
 */

/**
 * All time is Calendar instance, at least for now
 * In the future, we should better migrate to Java 8 LocalTime and ZonedDateTime
 *
 * Please migrate it if:
 * - app level build.gradle allow us to use `coreLibraryDesugaring` (to support API <26) or
 * - minSdkVersion >= 26
 */
sealed interface InteractiveUiModel {

    val id: Long
    val title: String

    data class Giveaway(
        override val id: Long,
        override val title: String,
        val status: Status,
    ) : InteractiveUiModel {

        sealed interface Status {

            data class Upcoming(
                val startTime: Calendar,
                val endTime: Calendar,
            ) : Status

            data class Ongoing(
                val endTime: Calendar,
            ) : Status

            object Finished : Status
            object Unknown : Status
        }
    }

    data class Quiz(
        override val id: Long,
        override val title: String,
        val status: Status,
    ) : InteractiveUiModel {

        sealed interface Status {

            data class Ongoing(val endTime: Calendar) : Status
            object Finished : Status
            object Unknown : Status
        }
    }

    object Unknown : InteractiveUiModel {
        override val id: Long
            get() = 0L

        override val title: String
            get() = ""
    }
}