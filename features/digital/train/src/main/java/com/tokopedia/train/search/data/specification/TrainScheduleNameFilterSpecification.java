package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;

import java.util.List;

/**
 * @author rizkyfadillah on 15/03/18.
 */

public class TrainScheduleNameFilterSpecification implements DbFlowSpecification {

    private List<String> trains;

    public TrainScheduleNameFilterSpecification(List<String> trains) {
        this.trains = trains;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        for (String trainName : trains) {
            conditions.or(TrainScheduleDbTable_Table.train_name.eq(trainName));
        }
        return conditions;
    }

}