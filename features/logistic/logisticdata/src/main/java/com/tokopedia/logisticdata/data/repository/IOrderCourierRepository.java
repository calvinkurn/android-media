package com.tokopedia.logisticdata.data.repository;

import com.tokopedia.logisticdata.data.entity.courierlist.CourierResponse;

import java.util.Map;

import rx.Observable;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public interface IOrderCourierRepository {

    Observable<CourierResponse> onOrderCourierRepository(String selectedCourierId,
                                                         Map<String, String> params);

    Observable<String> processShipping(Map<String, String> param);

    Observable<String> changeCourier(Map<String, String> param);

}
