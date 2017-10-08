package com.tokopedia.digital.product.domain;

import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.data.entity.response.ResponseLastOrderData;
import com.tokopedia.digital.product.data.entity.response.ResponseRecentNumberData;
import com.tokopedia.digital.product.data.mapper.IProductDigitalMapper;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class LastOrderNumberRepository implements ILastOrderNumberRepository {

    private final DigitalEndpointService digitalEndpointService;
    private final IProductDigitalMapper productDigitalMapper;

    public LastOrderNumberRepository(DigitalEndpointService digitalEndpointService,
                                     IProductDigitalMapper productDigitalMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.productDigitalMapper = productDigitalMapper;
    }

    @Override
    public Observable<OrderClientNumber> getLastOrder(TKPDMapParam<String, String> param
    ) {
        return digitalEndpointService.getApi().getLastOrder(param)
                .map(new Func1<Response<TkpdDigitalResponse>, OrderClientNumber>() {
                    @Override
                    public OrderClientNumber call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        ResponseLastOrderData lastOrderData =
                                tkpdDigitalResponseResponse.body().convertDataObj(
                                        ResponseLastOrderData.class
                                );
                        return productDigitalMapper.transformOrderClientNumber(lastOrderData);
                    }
                });
    }

    @Override
    public Observable<List<OrderClientNumber>> getRecentNumberOrderList(
            TKPDMapParam<String, String> param
    ) {
        return digitalEndpointService.getApi().getRecentNumber(param)
                .map(new Func1<Response<TkpdDigitalResponse>, List<OrderClientNumber>>() {
                    @Override
                    public List<OrderClientNumber> call(
                            Response<TkpdDigitalResponse> tkpdDigitalResponseResponse
                    ) {
                        List<ResponseRecentNumberData> responseRecentNumberDataList =
                                tkpdDigitalResponseResponse.body()
                                        .convertDataList(ResponseRecentNumberData[].class);
                        List<OrderClientNumber> orderClientNumberList = new ArrayList<>();
                        for (ResponseRecentNumberData data : responseRecentNumberDataList) {
                            orderClientNumberList.add(productDigitalMapper
                                    .transformOrderClientNumber(data));
                        }
                        return orderClientNumberList;
                    }
                });
    }
}
