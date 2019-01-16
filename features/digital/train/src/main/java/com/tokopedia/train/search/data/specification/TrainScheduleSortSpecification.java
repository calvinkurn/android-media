package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.tokopedia.common.travel.constant.TravelSortOption;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.common.specification.DbFlowWithOrderSpecification;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 15/03/18.
 */

public class TrainScheduleSortSpecification implements DbFlowWithOrderSpecification, DbFlowSpecification {

    private int sortOptionId;
    private int scheduleVariant;

    public TrainScheduleSortSpecification(int sortOptionId, int scheduleVariant) {
        this.sortOptionId = sortOptionId;
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.is_return_schedule.eq(scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE));
        return conditions;
    }

    @Override
    public List<OrderBy> toOrder() {
        List<OrderBy> orderBies = new ArrayList<OrderBy>();
        switch (sortOptionId) {
            case TravelSortOption.EARLIEST_DEPARTURE:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.departure_timestamp).ascending());
                break;
            case TravelSortOption.LATEST_DEPARTURE:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.departure_timestamp).descending());
                break;
            case TravelSortOption.SHORTEST_DURATION:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.duration).ascending());
                break;
            case TravelSortOption.LONGEST_DURATION:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.duration).descending());
                break;
            case TravelSortOption.EARLIEST_ARRIVAL:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.arrival_timestamp).ascending());
                break;
            case TravelSortOption.LATEST_ARRIVAL:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.arrival_timestamp).descending());
                break;
            case TravelSortOption.CHEAPEST:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.adult_fare).ascending());
                break;
            case TravelSortOption.MOST_EXPENSIVE:
                orderBies.add(OrderBy.fromProperty(TrainScheduleDbTable_Table.adult_fare).descending());
                break;
        }
        return orderBies;
    }

}
