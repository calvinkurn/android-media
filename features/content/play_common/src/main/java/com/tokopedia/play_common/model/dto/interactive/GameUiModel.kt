package com.tokopedia.play_common.model.dto.interactive

import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
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
sealed interface GameUiModel {

    val id: String
    val title: String
    val waitingDuration: Long

    data class Giveaway(
        override val id: String,
        override val title: String,
        override val waitingDuration: Long,
        val status: Status,
    ) : GameUiModel {

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
        override val id: String,
        override val title: String,
        override val waitingDuration: Long,
        val status: Status,
        val listOfChoices: List<QuizChoicesUiModel>,
    ) : GameUiModel {

        sealed interface Status {

            data class Ongoing(val endTime: Calendar) : Status
            object Failed: Status
            object Finished : Status
            object Unknown : Status
        }
    }

    object Unknown : GameUiModel {
        override val id: String
            get() = "0"

        override val title: String
            get() = ""

        override val waitingDuration: Long
            get() = 0L
    }
}
