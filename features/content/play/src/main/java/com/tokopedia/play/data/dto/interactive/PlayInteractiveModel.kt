package com.tokopedia.play.data.dto.interactive

/**
 * Created by jegul on 28/06/21
 */
sealed class PlayInteractiveModel {

    abstract val hasInteractive: Boolean

    data class Incomplete(override val hasInteractive: Boolean) : PlayInteractiveModel()
    data class Complete(override val hasInteractive: Boolean, ) : PlayInteractiveModel()
}

data class PlayCurrentInteractiveModel(
        val id: Long,
        val type: InteractiveType,
        val title: String,
        val timeStatus: PlayInteractiveTimeStatus
)

enum class InteractiveType(val type: Int) {
    QuickTap(0),
    Unknown(-1);

    companion object {

        private val values = values()

        fun getByValue(type: Int): InteractiveType {
            values.forEach {
                if (type == it.type) return it
            }
            return Unknown
        }
    }
}

sealed class PlayInteractiveTimeStatus {

    object Unknown : PlayInteractiveTimeStatus()
    /**
     * 1
     */
    data class Scheduled(val liveTimeInMs: Long, val durationInMs: Long) : PlayInteractiveTimeStatus()

    /**
     * 2
     */
    data class Live(val remainingTimeInMs: Long) : PlayInteractiveTimeStatus()

    /**
     * 3
     */
    object Finished : PlayInteractiveTimeStatus()

    companion object {

        fun getByValue(status: Int, countdownStartInSec: Int? = null, countdownEndInSec: Int? = null): PlayInteractiveTimeStatus {
            return when (status) {
                1 -> {
                    requireNotNull(countdownStartInSec)
                    requireNotNull(countdownEndInSec)
                    Scheduled(countdownStartInSec * 1000L, countdownEndInSec * 1000L)
                }
                2 -> {
                    requireNotNull(countdownEndInSec)
                    Live(countdownEndInSec * 1000L)
                }
                3 -> Finished
                else -> Unknown
            }
        }
    }
}