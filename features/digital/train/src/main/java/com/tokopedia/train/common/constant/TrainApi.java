package com.tokopedia.train.common.constant;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.train.search.data.entity.ScheduleAvailibilityDataEntity;
import com.tokopedia.train.search.data.entity.SearchDataEntity;
import com.tokopedia.train.station.data.entity.StationDataEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by alvarisi on 2/19/18.
 */

public interface TrainApi {

    @GET("stations")
    Observable<DataResponse<StationDataEntity>> stationsInIsland(@QueryMap Map<String, Object> params);

    @GET("schedules")
    Observable<DataResponse<SearchDataEntity>> schedulesTrain(@QueryMap Map<String, Object> params);

    @GET("schedules/availabilities/{idTrain}")
    Observable<DataResponse<ScheduleAvailibilityDataEntity>> availabilityTrain(@Path("idTrain") String idTrain);

}