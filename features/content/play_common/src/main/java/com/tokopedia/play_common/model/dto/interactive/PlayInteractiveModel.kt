package com.tokopedia.play_common.model.dto

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by jegul on 07/07/21
 */
data class PlayCurrentInteractiveModel(
        val id: Long = 0L,
        val type: InteractiveType = InteractiveType.Unknown,
        val title: String = "",
        val timeStatus: PlayInteractiveTimeStatus = PlayInteractiveTimeStatus.Unknown,
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
    data class Scheduled(val timeToStartInMs: Long, val interactiveDurationInMs: Long) : PlayInteractiveTimeStatus()

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
                0 -> {
                    requireNotNull(countdownStartInSec)
                    requireNotNull(countdownEndInSec)
                    require(countdownEndInSec > countdownStartInSec)
                    Scheduled(countdownStartInSec * 1000L, (countdownEndInSec - countdownStartInSec) * 1000L)
                }
                1 -> {
                    requireNotNull(countdownEndInSec)
                    Live(countdownEndInSec * 1000L)
                }
                2 -> Finished
                else -> Unknown
            }
        }
    }
}

@OptIn(ExperimentalContracts::class)
fun PlayInteractiveTimeStatus.isScheduled(): Boolean {
    contract {
        returns(true) implies (this@isScheduled is PlayInteractiveTimeStatus.Scheduled)
    }
    return this is PlayInteractiveTimeStatus.Scheduled
}