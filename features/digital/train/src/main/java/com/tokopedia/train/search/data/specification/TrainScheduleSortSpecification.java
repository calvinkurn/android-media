package com.tokopedia.train.search.data.specification;

import com.tokopedia.common.travel.constant.TravelSortOption;
import com.tokopedia.train.common.specification.RoomSpecification;
import com.tokopedia.train.common.specification.RoomWithOrderSpecification;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 15/03/18.
 */

public class TrainScheduleSortSpecification implements RoomWithOrderSpecification, RoomSpecification {

    private int sortOptionId;
    private int scheduleVariant;
    private List<Object> args;

    public TrainScheduleSortSpecification(int sortOptionId, int scheduleVariant) {
        this.sortOptionId = sortOptionId;
        this.scheduleVariant = scheduleVariant;
        args = new ArrayList<>();
    }

    @Override
    public String toOrder() {
        String query = "";
        switch (sortOptionId) {
            case TravelSortOption.EARLIEST_DEPARTURE:
                query += " ORDER BY departureTimestamp ASC";
                break;
            case TravelSortOption.LATEST_DEPARTURE:
                query += " ORDER BY departureTimestamp DESC";
                break;
            case TravelSortOption.SHORTEST_DURATION:
                query += " ORDER BY duration ASC";
                break;
            case TravelSortOption.LONGEST_DURATION:
                query += " ORDER BY duration DESC";
                break;
            case TravelSortOption.EARLIEST_ARRIVAL:
                query += " ORDER BY arrivalTimestamp ASC";
                break;
            case TravelSortOption.LATEST_ARRIVAL:
                query += " ORDER BY arrivalTimestamp DESC";
                break;
            case TravelSortOption.CHEAPEST:
                query += " ORDER BY adultFare ASC";
                break;
            case TravelSortOption.MOST_EXPENSIVE:
                query += " ORDER BY adultFare DESC";
                break;
        }
        return query;
    }

    @Override
    public String query() {
        args.clear();
        String query = " isReturnSchedule = ? " + toOrder();
        args.add(getScheduleVariant());
        return query;
    }

    @Override
    public List<Object> getArgs() {
        return args;
    }

    private int getScheduleVariant() {
        return scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE ? 1 : 0;
    }
}
