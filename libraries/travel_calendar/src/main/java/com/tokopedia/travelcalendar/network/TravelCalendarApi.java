package com.tokopedia.travelcalendar.network;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.travelcalendar.data.entity.HolidayEntity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public interface TravelCalendarApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<GraphqlResponse<HolidayEntity>> getHolidayEntity(@Body Map<String, Object> requestBodyMap);
}
