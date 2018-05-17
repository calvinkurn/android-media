package com.tokopedia.train.search.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.specification.CloudNetworkSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.search.data.entity.AvailabilityEntity;
import com.tokopedia.train.search.data.entity.ScheduleAvailibilityDataEntity;
import com.tokopedia.train.search.data.entity.ScheduleEntity;
import com.tokopedia.train.search.data.entity.SearchDataEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleCloudDataStore {

    private TrainApi trainApi;

    public TrainScheduleCloudDataStore(TrainApi trainApi) {
        this.trainApi = trainApi;
    }

    public Observable<List<ScheduleEntity>> getDatasSchedule(Specification specification) {
        return trainApi.schedulesTrain(((CloudNetworkSpecification) specification).networkParam())
                .map(new Func1<DataResponse<SearchDataEntity>, List<ScheduleEntity>>() {
                    @Override
                    public List<ScheduleEntity> call(DataResponse<SearchDataEntity> response) {
                        return response.getData().getSchedules();
                    }
                });
    }

    public Observable<List<AvailabilityEntity>> getDatasAvailability(String idTrain) {
        return trainApi.availabilityTrain(idTrain)
                .map(new Func1<DataResponse<ScheduleAvailibilityDataEntity>, List<AvailabilityEntity>>() {
                    @Override
                    public List<AvailabilityEntity> call(DataResponse<ScheduleAvailibilityDataEntity> listDataResponse) {
                        return listDataResponse.getData().getAvailabilities();
                    }
                });
    }
}
