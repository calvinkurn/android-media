package com.tokopedia.train.search.domain.mapper;

import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.search.data.database.TrainScheduleTable;
import com.tokopedia.train.search.data.entity.FareEntity;
import com.tokopedia.train.search.data.entity.ScheduleEntity;
import com.tokopedia.train.search.data.entity.TrainScheduleEntity;
import com.tokopedia.train.search.data.typedef.TrainAvailabilityTypeDef;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainScheduleMapper {

    @Inject
    public TrainScheduleMapper() {
    }

    public List<TrainScheduleViewModel> transformToModel(List<TrainScheduleTable> trainScheduleTables) {
        List<TrainScheduleViewModel> trainScheduleViewModelList = new ArrayList<>();
        List<TrainScheduleViewModel> trainScheduleNotAvailable = new ArrayList<>();
        TrainScheduleViewModel trainScheduleViewModel = null;
        if (trainScheduleTables != null) {
            for (TrainScheduleTable trainScheduleDbTable : trainScheduleTables) {
                if (trainScheduleDbTable != null) {
                    trainScheduleViewModel = transform(trainScheduleDbTable);
                    if (trainScheduleViewModel != null) {
                        if (trainScheduleViewModel.getAvailableSeat() > 0) {
                            trainScheduleViewModelList.add(trainScheduleViewModel);
                        } else {
                            trainScheduleNotAvailable.add(trainScheduleViewModel);
                        }
                    }
                }
            }
        }
        trainScheduleViewModelList.addAll(trainScheduleNotAvailable);
        return trainScheduleViewModelList;
    }

    public TrainScheduleViewModel transform(TrainScheduleTable trainScheduleDbTable) {
        TrainScheduleViewModel trainScheduleViewModel = null;
        if (trainScheduleDbTable != null) {
            trainScheduleViewModel = new TrainScheduleViewModel();
            trainScheduleViewModel.setIdSchedule(trainScheduleDbTable.getIdSchedule());
            trainScheduleViewModel.setAdultFare(trainScheduleDbTable.getAdultFare());
            trainScheduleViewModel.setDisplayAdultFare(trainScheduleDbTable.getDisplayAdultFare());
            trainScheduleViewModel.setInfantFare(trainScheduleDbTable.getInfantFare());
            trainScheduleViewModel.setDisplayInfantFare(trainScheduleDbTable.getDisplayInfantFare());
            trainScheduleViewModel.setArrivalTimestamp(trainScheduleDbTable.getArrivalTimestamp());
            trainScheduleViewModel.setDepartureTimestamp(trainScheduleDbTable.getDepartureTimestamp());
            trainScheduleViewModel.setClassTrain(trainScheduleDbTable.getClassTrain());
            trainScheduleViewModel.setDisplayClass(trainScheduleDbTable.getDisplayClass());
            trainScheduleViewModel.setSubclass(trainScheduleDbTable.getSubclass());
            trainScheduleViewModel.setOrigin(trainScheduleDbTable.getOrigin());
            trainScheduleViewModel.setDestination(trainScheduleDbTable.getDestination());
            trainScheduleViewModel.setDisplayDuration(trainScheduleDbTable.getDisplayDuration());
            trainScheduleViewModel.setDuration(trainScheduleDbTable.getDuration());
            trainScheduleViewModel.setTrainKey(trainScheduleDbTable.getTrainKey());
            trainScheduleViewModel.setTrainName(trainScheduleDbTable.getTrainName());
            trainScheduleViewModel.setTrainNumber(trainScheduleDbTable.getTrainNumber());
            trainScheduleViewModel.setAvailableSeat(trainScheduleDbTable.getAvailableSeat());
            trainScheduleViewModel.setCheapestFlag(trainScheduleDbTable.isCheapestFlag());
            trainScheduleViewModel.setFastestFlag(trainScheduleDbTable.isFastestFlag());
            trainScheduleViewModel.setReturnTrip(trainScheduleDbTable.isReturnSchedule());
        }
        return trainScheduleViewModel;
    }

    public List<TrainScheduleTable> transformToTable(List<ScheduleEntity> scheduleEntities, int scheduleVariant) {
        List<TrainScheduleTable> trainScheduleTables = new ArrayList<>();
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            for (TrainScheduleEntity trainScheduleEntity : scheduleEntity.getTrains()) {
                for (FareEntity fareEntity : trainScheduleEntity.getFares()) {
                    TrainScheduleTable trainScheduleTable = new TrainScheduleTable();
                    trainScheduleTable.setIdSchedule(fareEntity.getId());
                    trainScheduleTable.setAdultFare(fareEntity.getAdultFare());
                    trainScheduleTable.setDisplayAdultFare(fareEntity.getDisplayAdultFare());
                    trainScheduleTable.setInfantFare(fareEntity.getInfantFare());
                    trainScheduleTable.setDisplayInfantFare(fareEntity.getDisplayInfantFare());
                    trainScheduleTable.setArrivalTimestamp(trainScheduleEntity.getArrivalTimestamp());
                    trainScheduleTable.setDepartureTimestamp(trainScheduleEntity.getDepartureTimestamp());
                    trainScheduleTable.setClassTrain(fareEntity.getScheduleClass());
                    trainScheduleTable.setDisplayClass(fareEntity.getDisplayClass());
                    trainScheduleTable.setSubclass(fareEntity.getSubclass());
                    trainScheduleTable.setOrigin(scheduleEntity.getOrigin());
                    trainScheduleTable.setDestination(scheduleEntity.getDestination());
                    trainScheduleTable.setDisplayDuration(trainScheduleEntity.getDisplayDuration());
                    trainScheduleTable.setDuration(trainScheduleEntity.getDuration());
                    trainScheduleTable.setTrainKey(trainScheduleEntity.getTrainKey());
                    trainScheduleTable.setTrainName(trainScheduleEntity.getTrainName());
                    trainScheduleTable.setTrainNumber(trainScheduleEntity.getTrainNo());
                    trainScheduleTable.setAvailableSeat(TrainAvailabilityTypeDef.DEFAULT_VALUE);
                    trainScheduleTable.setCheapestFlag(false);
                    trainScheduleTable.setFastestFlag(false);
                    trainScheduleTable.setReturnSchedule(scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE);

                    String departureHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                            TrainDateUtil.FORMAT_HOUR, trainScheduleEntity.getDepartureTimestamp());
                    trainScheduleTable.setDepartureHour(Integer.valueOf(departureHour));

                    String departureTimestampCustom = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                            TrainDateUtil.DEFAULT_TIMESTAMP_FORMAT, trainScheduleEntity.getDepartureTimestamp());
                    trainScheduleTable.setDepartureTime(convertTimestampToLong(departureTimestampCustom));

                    trainScheduleTables.add(trainScheduleTable);
                }
            }
        }
        return trainScheduleTables;
    }

    private long convertTimestampToLong(String timeString) {
        Timestamp timestamp = Timestamp.valueOf(timeString);
        return timestamp.getTime();
    }

    public List<AvailabilityKeySchedule> transformToAvailabilityKey(List<ScheduleEntity> scheduleEntities) {
        List<AvailabilityKeySchedule> availabilityKeySchedules = new ArrayList<>();
        if (scheduleEntities != null) {
            for (ScheduleEntity scheduleEntity : scheduleEntities) {
                for (TrainScheduleEntity trainScheduleEntity : scheduleEntity.getTrains()) {
                    AvailabilityKeySchedule availabilityKeySchedule = new AvailabilityKeySchedule();
                    availabilityKeySchedule.setIdTrain(trainScheduleEntity.getId());
                    availabilityKeySchedules.add(availabilityKeySchedule);
                }
            }
        }
        return availabilityKeySchedules;
    }
}
