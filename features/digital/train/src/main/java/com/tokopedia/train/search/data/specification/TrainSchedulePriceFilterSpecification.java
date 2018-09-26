package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;

/**
 * @author rizkyfadillah on 14/03/18.
 */

public class TrainSchedulePriceFilterSpecification implements DbFlowSpecification {

    private long minPriceFilterValue;
    private long maxPriceFilterValue;

    public TrainSchedulePriceFilterSpecification(long minPriceFilterValue, long maxPriceFilterValue) {
        this.minPriceFilterValue = minPriceFilterValue;
        this.maxPriceFilterValue = maxPriceFilterValue;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.adult_fare.lessThanOrEq(maxPriceFilterValue))
                .and(TrainScheduleDbTable_Table.adult_fare.greaterThanOrEq(minPriceFilterValue));
        return conditions;
    }

}
