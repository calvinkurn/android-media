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
    private int scheduleVariant;
    private long arrivalTimestampSelected = 0;

    public TrainAvailabilitySpecification(List<AvailabilityEntity> scheduleAvailabilityEntities, int scheduleVariant, String arrivalTimestampSelected) {
        this.scheduleAvailabilityEntities = scheduleAvailabilityEntities;
        this.scheduleVariant = scheduleVariant;
        setArrivalTimestampSelected(arrivalTimestampSelected);
    }

    public void setArrivalTimestampSelected(String arrivalTimestampSelected) {
        if (arrivalTimestampSelected != null) {
            String arrivalTimestampCustom = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                    TrainDateUtil.DEFAULT_TIMESTAMP_FORMAT, arrivalTimestampSelected);
            Timestamp timestamp = Timestamp.valueOf(arrivalTimestampCustom);
            this.arrivalTimestampSelected = timestamp.getTime();
        }
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        for (AvailabilityEntity availabilityEntity : scheduleAvailabilityEntities) {
            conditionGroup.or(TrainScheduleDbTable_Table.schedule_id.eq(availabilityEntity.getId()));
            if (isReturnSchedule()) {
                conditionGroup.and(TrainScheduleDbTable_Table.is_return_schedule.eq(true));
                conditionGroup.and(TrainScheduleDbTable_Table.departure_time.greaterThan(arrivalTimestampSelected));
            }
        }
        return conditionGroup;
    }

    private boolean isReturnSchedule() {
        return scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE;
    }

}
