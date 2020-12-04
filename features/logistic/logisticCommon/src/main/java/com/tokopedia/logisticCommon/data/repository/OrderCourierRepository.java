package com.tokopedia.logisticCommon.data.repository;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticCommon.data.apiservice.MyShopOrderActApi;
import com.tokopedia.logisticCommon.data.apiservice.MyShopOrderApi;
import com.tokopedia.logisticCommon.data.apiservice.OrderDetailApi;
import com.tokopedia.logisticCommon.data.entity.courierlist.CourierResponse;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class OrderCourierRepository implements IOrderCourierRepository {

    private MyShopOrderApi myShopOrderApi;

    private MyShopOrderActApi myShopOrderActApi;

    private OrderDetailApi orderDetailApi;

    public OrderCourierRepository(MyShopOrderApi myShopOrderApi,
                                  MyShopOrderActApi myShopOrderActApi,
                                  OrderDetailApi orderDetailApi) {
        this.myShopOrderApi = myShopOrderApi;
        this.myShopOrderActApi = myShopOrderActApi;
        this.orderDetailApi = orderDetailApi;
    }

    @Override
    public Observable<CourierResponse> onOrderCourierRepository(
            final String selectedCourierId,
            Map<String, String> params
    ) {
        return myShopOrderApi.getEditShippingForm(params)
                .map(tkpdResponseResponse -> tkpdResponseResponse.body().convertDataObj(CourierResponse.class));
    }

    @Override
    public Observable<String> processShipping(Map<String, String> param) {
        return myShopOrderActApi.proceedShipping(param)
                .map(this::displayMessageToUser);
    }

    @Override
    public Observable<String> changeCourier(Map<String, String> param) {
        return orderDetailApi.changeCourier(param)
                .map(this::displayMessageToUser);
    }

    private String displayMessageToUser(Response<TokopediaWsV4Response> tkpdResponseResponse) {
        if (tkpdResponseResponse.isSuccessful() && !tkpdResponseResponse.body().isError())
            return tkpdResponseResponse.body().getStatusMessageJoined();
        else
            throw new RuntimeException(
                    tkpdResponseResponse.body().getErrorMessageJoined()
            );
    }
}
