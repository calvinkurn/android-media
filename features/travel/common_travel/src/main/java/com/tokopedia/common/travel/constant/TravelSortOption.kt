package com.tokopedia.common.travel.constant

import androidx.annotation.IntDef

/**
 * Created by nabillasabbaha on 28/08/18.
 */
@IntDef(value = [TravelSortOption.NO_PREFERENCE, TravelSortOption.CHEAPEST,
    TravelSortOption.MOST_EXPENSIVE, TravelSortOption.EARLIEST_DEPARTURE,
    TravelSortOption.LATEST_DEPARTURE, TravelSortOption.SHORTEST_DURATION,
    TravelSortOption.LONGEST_DURATION, TravelSortOption.EARLIEST_ARRIVAL,
    TravelSortOption.LATEST_ARRIVAL])
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class TravelSortOption {
    companion object {
        const val NO_PREFERENCE = 0
        const val CHEAPEST = 1
        const val MOST_EXPENSIVE = 2
        const val EARLIEST_DEPARTURE = 3
        const val LATEST_DEPARTURE = 4
        const val SHORTEST_DURATION = 5
        const val LONGEST_DURATION = 6
        const val EARLIEST_ARRIVAL = 7
        const val LATEST_ARRIVAL = 8
    }
}