package com.tokopedia.train.search.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.CloudNetworkSpecification;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;

import java.util.Map;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainScheduleSpecification implements DbFlowSpecification, CloudNetworkSpecification {

    private Map<String, Object> mapParam;
    private int scheduleVariant;

    public TrainScheduleSpecification(Map<String, Object> mapParam) {
        this.mapParam = mapParam;
    }

    public void setScheduleVariant(int scheduleVariant) {
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public Map<String, Object> networkParam() {
        return mapParam;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.or(TrainScheduleDbTable_Table.is_return_schedule.eq(isReturnSchedule()));
        return conditionGroup;
    }

    private boolean isReturnSchedule() {
        return scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE;
    }
}
