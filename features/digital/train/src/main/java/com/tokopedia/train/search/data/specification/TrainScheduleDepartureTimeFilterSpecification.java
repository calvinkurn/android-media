package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.train.search.data.typedef.DepartureTimeTypeDef;

import java.util.List;

/**
 * @author rizkyfadillah on 15/03/18.
 */

public class TrainScheduleDepartureTimeFilterSpecification implements DbFlowSpecification {

    private List<String> departureTrains;

    public TrainScheduleDepartureTimeFilterSpecification(List<String> departureTrains) {
        this.departureTrains = departureTrains;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        for (int i = 0; i < departureTrains.size(); i++) {
            String departureTrain = departureTrains.get(i);
            switch (departureTrain) {
                case DepartureTimeTypeDef.MORNING:
                    conditions.or(TrainScheduleDbTable_Table.departure_hour.between(00).and(12));
                    break;
                case DepartureTimeTypeDef.AFTERNOON:
                    conditions.or(TrainScheduleDbTable_Table.departure_hour.between(12).and(18));
                    break;
                case DepartureTimeTypeDef.NIGHT:
                    conditions.or(TrainScheduleDbTable_Table.departure_hour.between(18).and(00));
                    break;
            }
        }
        return conditions;
    }
}
