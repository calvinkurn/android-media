package com.tokopedia.logisticinputreceiptshipment.confirmshipment;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticinputreceiptshipment.network.apiservice.MyShopOrderActApi;
import com.tokopedia.logisticinputreceiptshipment.network.apiservice.MyShopOrderApi;
import com.tokopedia.logisticinputreceiptshipment.network.apiservice.OrderDetailApi;
import com.tokopedia.logisticinputreceiptshipment.network.mapper.OrderDetailMapper;
import com.tokopedia.logisticinputreceiptshipment.network.response.courierlist.CourierResponse;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.transaction.common.data.order.ListCourierViewModel;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class OrderCourierRepository implements IOrderCourierRepository {

    private OrderDetailMapper mapper;

    private MyShopOrderApi myShopOrderApi;

    private MyShopOrderActApi myShopOrderActApi;

    private OrderDetailApi orderDetailApi;

    public OrderCourierRepository(OrderDetailMapper mapper,
                                  MyShopOrderApi myShopOrderApi,
                                  MyShopOrderActApi myShopOrderActApi,
                                  OrderDetailApi orderDetailApi) {
        this.mapper = mapper;
        this.myShopOrderApi = myShopOrderApi;
        this.myShopOrderActApi = myShopOrderActApi;
        this.orderDetailApi = orderDetailApi;
    }

    @Override
    public Observable<ListCourierViewModel> onOrderCourierRepository(
            final String selectedCourierId,
            TKPDMapParam<String, String> params
    ) {
        return myShopOrderApi.getEditShippingForm(params)
                .map(tkpdResponseResponse -> mapper.getCourierServiceModel(
                        new Gson().fromJson(tkpdResponseResponse.body().getStringData(),
                                CourierResponse.class),
                        selectedCourierId
                ));
    }

    @Override
    public Observable<String> processShipping(TKPDMapParam<String, String> param) {
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
