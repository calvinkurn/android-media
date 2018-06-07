package com.tokopedia.train.common.constant;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.train.seat.data.entity.TrainSeatsEntity;
import com.tokopedia.train.search.data.entity.ScheduleAvailabilityResponse;
import com.tokopedia.train.search.data.entity.SearchDataResponse;
import com.tokopedia.train.station.data.entity.StationDataEntity;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by alvarisi on 2/19/18.
 */

public interface TrainApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<StationDataEntity>> stationsInIsland(@Body Map<String, Object> params);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<SearchDataResponse>> schedulesTrain(@Body Map<String, Object> params);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<ScheduleAvailabilityResponse>> availabilityTrain(@Body Map<String, Object> params);



    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<TrainSeatsEntity>> seats(@Body Map<String, Object> params);

}