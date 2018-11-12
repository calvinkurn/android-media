package com.tokopedia.common.travel.data;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;

/**
 * Created by nabillasabbaha on 12/11/18.
 */
public class TravelPassengerListSpecification implements DbFlowSpecification {

    public TravelPassengerListSpecification() {
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        return conditionGroup;
    }
}
