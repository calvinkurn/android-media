package com.tokopedia.play_common.model.dto.interactive

/**
 * Created by kenny.hadisaputra on 12/04/22
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
                val timeToStartInMs: Long,
                val durationInMs: Long,
            ) : Status

            data class Ongoing(
                val durationInMs: Long,
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

            data class Ongoing(val durationInMs: Long) : Status
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