package com.tokopedia.train.common.constant;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.train.checkout.data.entity.TrainCheckoutWrapperEntity;
import com.tokopedia.train.homepage.data.entity.TrainBannerEntity;
import com.tokopedia.train.passenger.data.cloud.entity.TrainSoftbookWrapperEntity;
import com.tokopedia.train.reviewdetail.data.entity.TrainCheckVoucherWrapperEntity;
import com.tokopedia.train.search.data.entity.ScheduleAvailabilityResponse;
import com.tokopedia.train.search.data.entity.SearchDataResponse;
import com.tokopedia.train.seat.data.entity.TrainChangeSeatsDataEntity;
import com.tokopedia.train.seat.data.entity.TrainKaiSeatMapEntity;
import com.tokopedia.train.station.data.entity.StationDataEntity;

import java.util.HashMap;
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
    Observable<DataResponse<TrainKaiSeatMapEntity>> seats(@Body Map<String, Object> params);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<TrainChangeSeatsDataEntity>> changeSeats(@Body HashMap<String, Object> parameters);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<TrainSoftbookWrapperEntity>> doSoftBooking(@Body Map<String, Object> params);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<TrainCheckVoucherWrapperEntity>> checkVoucher(@Body Map<String, Object> params);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<TrainCheckoutWrapperEntity>> checkout(@Body Map<String, Object> params);

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<DataResponse<TrainBannerEntity>> banners(@Body Map<String, Object> params);

}