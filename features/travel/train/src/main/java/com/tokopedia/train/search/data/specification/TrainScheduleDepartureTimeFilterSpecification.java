package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;

/**
 * @author rizkyfadillah on 15/03/18.
 */

public class TrainScheduleDepartureTimeFilterSpecification implements DbFlowSpecification {

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        return conditions;
    }

}
