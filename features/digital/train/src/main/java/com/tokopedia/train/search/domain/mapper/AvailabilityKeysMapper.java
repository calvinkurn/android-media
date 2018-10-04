package com.tokopedia.train.search.domain.mapper;

import com.tokopedia.train.search.data.entity.ScheduleEntity;
import com.tokopedia.train.search.data.entity.TrainScheduleEntity;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class AvailabilityKeysMapper implements Func1<List<ScheduleEntity>, List<AvailabilityKeySchedule>> {

    public AvailabilityKeysMapper() {
    }

    @Override
    public List<AvailabilityKeySchedule> call(List<ScheduleEntity> scheduleEntities) {
        List<AvailabilityKeySchedule> availabilityKeySchedules = new ArrayList<>();
        if (scheduleEntities != null) {
            for (ScheduleEntity scheduleEntity : scheduleEntities) {
                for (TrainScheduleEntity trainScheduleEntity: scheduleEntity.getTrains()) {
                    AvailabilityKeySchedule availabilityKeySchedule = new AvailabilityKeySchedule();
                    availabilityKeySchedule.setIdTrain(trainScheduleEntity.getId());
                    availabilityKeySchedules.add(availabilityKeySchedule);
                }
            }
        }
        return availabilityKeySchedules;
    }
}
