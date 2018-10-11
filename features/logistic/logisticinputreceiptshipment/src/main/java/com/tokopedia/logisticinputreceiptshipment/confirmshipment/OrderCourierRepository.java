package com.tokopedia.logisticinputreceiptshipment.confirmshipment;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.logisticinputreceiptshipment.network.MyShopOrderActService;
import com.tokopedia.logisticinputreceiptshipment.network.mapper.OrderDetailMapper;
import com.tokopedia.logisticinputreceiptshipment.network.response.courierlist.CourierResponse;
import com.tokopedia.transaction.common.data.order.ListCourierViewModel;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class OrderCourierRepository implements IOrderCourierRepository {

    private OrderDetailMapper mapper;

    private MyShopOrderService service;

    private MyShopOrderActService actionService;

    private OrderDetailService orderDetailService;

    public OrderCourierRepository(OrderDetailMapper mapper,
                                  MyShopOrderService shopService,
                                  MyShopOrderActService actionService,
                                  OrderDetailService orderDetailService) {
        this.mapper = mapper;
        this.service = shopService;
        this.actionService = actionService;
        this.orderDetailService = orderDetailService;
    }

    @Override
    public Observable<ListCourierViewModel> onOrderCourierRepository(
            final String selectedCourierId,
            TKPDMapParam<String, String> params
    ) {
        return service.getApi().getEditShippingForm(params)
                .map(tkpdResponseResponse -> mapper.getCourierServiceModel(
                        new Gson().fromJson(tkpdResponseResponse.body().getStringData(),
                                CourierResponse.class),
                        selectedCourierId
                ));
    }

    @Override
    public Observable<String> processShipping(TKPDMapParam<String, String> param) {
        return actionService.getApi().proceedShipping(param)
                .map(this::displayMessageToUser);
    }

    @Override
    public Observable<String> changeCourier(Map<String, String> param) {
        return orderDetailService.getApi().changeCourier(param)
                .map(this::displayMessageToUser);
    }

    private String displayMessageToUser(Response<TkpdResponse> tkpdResponseResponse) {
        if (tkpdResponseResponse.isSuccessful() && !tkpdResponseResponse.body().isError())
            return tkpdResponseResponse.body().getStatusMessageJoined();
        else
            throw new RuntimeException(
                    tkpdResponseResponse.body().getErrorMessageJoined()
            );
    }
}
