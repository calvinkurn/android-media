package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.train.search.data.entity.AvailabilityEntity;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainAvailabilitySpecification implements DbFlowSpecification {

    private List<AvailabilityEntity> scheduleAvailabilityEntities;

    public TrainAvailabilitySpecification(List<AvailabilityEntity> scheduleAvailabilityEntities) {
        this.scheduleAvailabilityEntities = scheduleAvailabilityEntities;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        for (AvailabilityEntity availabilityEntity : scheduleAvailabilityEntities) {
            conditionGroup.or(TrainScheduleDbTable_Table.schedule_id.eq(availabilityEntity.getId()));
        }
        return conditionGroup;
    }

}
