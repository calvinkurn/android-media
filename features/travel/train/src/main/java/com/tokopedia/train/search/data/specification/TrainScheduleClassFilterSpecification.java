package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;

import java.util.List;

/**
 * @author rizkyfadillah on 14/03/18.
 */

public class TrainScheduleClassFilterSpecification implements DbFlowSpecification {

    private List<String> trainClasses;

    public TrainScheduleClassFilterSpecification(List<String> trainClasses) {
        this.trainClasses = trainClasses;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        for (String trainClass : trainClasses) {
            conditions.or(TrainScheduleDbTable_Table.display_class.eq(trainClass));
        }
        return conditions;
    }

}
