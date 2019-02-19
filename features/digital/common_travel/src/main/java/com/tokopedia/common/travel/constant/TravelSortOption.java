package com.tokopedia.common.travel.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nabillasabbaha on 28/08/18.
 */
@IntDef({
        TravelSortOption.NO_PREFERENCE,
        TravelSortOption.EARLIEST_DEPARTURE,
        TravelSortOption.LATEST_DEPARTURE,
        TravelSortOption.EARLIEST_ARRIVAL,
        TravelSortOption.LATEST_ARRIVAL,
        TravelSortOption.SHORTEST_DURATION,
        TravelSortOption.LONGEST_DURATION,
        TravelSortOption.CHEAPEST,
        TravelSortOption.MOST_EXPENSIVE})
@Retention(RetentionPolicy.SOURCE)
public @interface TravelSortOption {
    int NO_PREFERENCE = 0;
    int CHEAPEST = 1;
    int MOST_EXPENSIVE = 2;
    int EARLIEST_DEPARTURE = 3;
    int LATEST_DEPARTURE = 4;
    int SHORTEST_DURATION = 5;
    int LONGEST_DURATION = 6;
    int EARLIEST_ARRIVAL = 7;
    int LATEST_ARRIVAL = 8;

}
