package com.tokopedia.train.search.data.specification;

import com.tokopedia.train.common.specification.RoomSpecification;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 06/07/18.
 */
public class TrainSearchScheduleSpecification implements RoomSpecification {

    private int scheduleVariant;
    private long arrivalTimestampSelected = 0;
    private List<Object> args;

    public TrainSearchScheduleSpecification(int scheduleVariant, String arrivalTimestampStringSelected) {
        this.scheduleVariant = scheduleVariant;
        args = new ArrayList<>();
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

    private boolean isReturnSchedule() {
        return scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE;
    }

    @Override
    public String query() {
        String query = "";
        args.clear();
        if (isReturnSchedule()) {
            query = " isReturnSchedule = ? AND departureTime > ? ";
            args.add(1);
            args.add(arrivalTimestampSelected);
        }
        return query;
    }

    @Override
    public List<Object> getArgs() {
        return args;
    }
}
