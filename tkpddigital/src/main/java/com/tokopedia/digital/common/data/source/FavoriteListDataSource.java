package com.tokopedia.digital.common.data.source;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.common.data.mapper.FavoriteNumberListDataMapper;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteList;
import com.tokopedia.digital.widget.data.entity.response.ResponseFavoriteNumber;
import com.tokopedia.digital.widget.data.entity.response.ResponseMetaFavoriteNumber;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author rizkyfadillah on 19/01/18.
 */

public class FavoriteListDataSource {

    private DigitalEndpointService digitalEndpointService;
    private final FavoriteNumberListDataMapper favoriteNumberMapper;

    public FavoriteListDataSource(DigitalEndpointService digitalEndpointService,
                                  FavoriteNumberListDataMapper favoriteNumberMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.favoriteNumberMapper = favoriteNumberMapper;
    }

    public Observable<DigitalNumberList> getFavoriteList(TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi()
                .getFavoriteList(param)
                .map(getFuncTransformNumberList())
                .onErrorReturn(new Func1<Throwable, DigitalNumberList>() {
                    @Override
                    public DigitalNumberList call(Throwable throwable) {
                        return new DigitalNumberList(new ArrayList<OrderClientNumber>(), null);
                    }
                });
    }

    private Func1<Response<TkpdDigitalResponse>, DigitalNumberList> getFuncTransformNumberList() {
        return new Func1<Response<TkpdDigitalResponse>, DigitalNumberList>() {
            @Override
            public DigitalNumberList call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                List<ResponseFavoriteNumber> responseFavoriteNumbers = tkpdDigitalResponseResponse
                        .body().convertDataList(ResponseFavoriteNumber[].class);

                ResponseMetaFavoriteNumber responseMetaFavoriteNumber =
                        tkpdDigitalResponseResponse.body().convertMetaObj(ResponseMetaFavoriteNumber.class);

                ResponseFavoriteList responseFavoriteList =  new ResponseFavoriteList(responseMetaFavoriteNumber,
                        responseFavoriteNumbers);

                return favoriteNumberMapper
                        .transformDigitalFavoriteNumberItemDataList(responseFavoriteList);
            }
        };
    }

}
