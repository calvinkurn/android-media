package com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment;


import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.ListCourierViewModel;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public interface OrderCourierInteractor {

    void onGetCourierList(
            String selectedCourierId,
            Map<String, String> params,
            Subscriber<ListCourierViewModel> model
    );

    void confirmShipping(Map<String, String> params, Subscriber<String> subscriber);

    void changeCourier(Map<String, String> params, Subscriber<String> subscriber);

}
