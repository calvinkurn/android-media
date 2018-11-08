package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;

import java.sql.Timestamp;

/**
 * Created by nabillasabbaha on 06/07/18.
 */
public class TrainSearchScheduleSpecification implements DbFlowSpecification {

    private int scheduleVariant;
    private long arrivalTimestampSelected = 0;

    public TrainSearchScheduleSpecification(int scheduleVariant, String arrivalTimestampStringSelected) {
        this.scheduleVariant = scheduleVariant;
        setArrivalTimestampSelected(arrivalTimestampStringSelected);
    }

    public void setArrivalTimestampSelected(String arrivalTimestampSelected) {
        if (arrivalTimestampSelected != null) {
            String arrivalTimestampCustom = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                    TrainDateUtil.DEFAULT_TIMESTAMP_FORMAT, arrivalTimestampSelected);
            Timestamp timestamp = Timestamp.valueOf(arrivalTimestampCustom);
            this.arrivalTimestampSelected = timestamp.getTime();
        }
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        if (isReturnSchedule()) {
            conditionGroup.and(TrainScheduleDbTable_Table.is_return_schedule.eq(true));
            conditionGroup.and(TrainScheduleDbTable_Table.departure_time.greaterThan(arrivalTimestampSelected));
        }
        return conditionGroup;
    }

    private boolean isReturnSchedule() {
        return scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE;
    }
}
