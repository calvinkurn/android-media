package com.tokopedia.common.travel.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.common.travel.database.TravelPassengerDb_Table;

/**
 * Created by nabillasabbaha on 12/11/18.
 */
public class TravelPassengerUpsertSpecification implements DbFlowSpecification {

    private String idPassengerPrevious;

    public TravelPassengerUpsertSpecification(String idPassengerPrevious) {
        this.idPassengerPrevious = idPassengerPrevious;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.or(TravelPassengerDb_Table.idPassenger.eq(idPassengerPrevious));
        return conditions;
    }
}
