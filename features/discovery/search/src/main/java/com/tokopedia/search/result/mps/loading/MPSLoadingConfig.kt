package com.tokopedia.search.result.mps.loading

import kotlin.random.Random

data class MPSLoadingConfig(
    val delay: Long,
    val randomIncrement: () -> Int,
    val pauseProgressThreshold: Int,
) {

    companion object {
        const val DEFAULT_DELAY = 1_000L
        const val BASE_PROGRESS_INCREMENT = 10
        const val RANDOM_PROGRESS_MULTIPLIER = 5
        const val RANDOM_PROGRESS_UNTIL = 2
        const val PAUSE_PROGRESS_THRESHOLD = 85

        fun default(): MPSLoadingConfig =
            MPSLoadingConfig(
                DEFAULT_DELAY,
                defaultRandomConfig(),
                PAUSE_PROGRESS_THRESHOLD
            )

        fun defaultRandomConfig(): () -> Int = {
            BASE_PROGRESS_INCREMENT +
                RANDOM_PROGRESS_MULTIPLIER * Random.nextInt(RANDOM_PROGRESS_UNTIL)
        }
    }
}
