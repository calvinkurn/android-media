package com.tokopedia.flight.orderlist.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.orderlist.constant.FlightOrderUrl;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightOrderApi {

    @GET(FlightOrderUrl.FLIGHT_ORDERS)
    Observable<Response<DataResponse<List<OrderEntity>>>> getOrders(@QueryMap Map<String, Object> paramsAllValueInString);

    @GET(FlightOrderUrl.FLIGHT_ORDER)
    Observable<Response<DataResponse<OrderEntity>>> getOrder(@Path("id") String id);

    @GET(FlightOrderUrl.FLIGHT_EMAIL)
    Observable<Response<SendEmailEntity>> sendEmail(@QueryMap Map<String, Object> param);

}
