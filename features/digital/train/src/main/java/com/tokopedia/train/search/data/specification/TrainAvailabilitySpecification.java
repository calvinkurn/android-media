package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.train.search.data.entity.AvailabilityEntity;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainAvailabilitySpecification implements DbFlowSpecification {

    private List<AvailabilityEntity> scheduleAvailabilityEntities;
    private int scheduleVariant;

    public TrainAvailabilitySpecification(List<AvailabilityEntity> availabilityEntities,
                                          int scheduleVariant) {
        this.scheduleAvailabilityEntities = availabilityEntities;
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        for (AvailabilityEntity availabilityEntity : scheduleAvailabilityEntities) {
            conditionGroup.or(TrainScheduleDbTable_Table.schedule_id.eq(availabilityEntity.getId()));
            if (isReturnSchedule()) {
                conditionGroup.or(TrainScheduleDbTable_Table.is_return_schedule.eq(true));
            }

        }
        return conditionGroup;
    }

    private boolean isReturnSchedule() {
        return scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE;
    }

}
