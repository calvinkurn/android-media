package com.tokopedia.flight.review.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.review.data.model.AttributesVoucher;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 11/27/17.
 */

public class FlightCheckVoucherCodeDataSourceCloud {

    private final FlightApi flightApi;

    @Inject
    public FlightCheckVoucherCodeDataSourceCloud(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<AttributesVoucher> checkVoucherCode(HashMap<String, String> paramsAllValueInString) {
        return flightApi.checkVoucherCode(paramsAllValueInString)
                .flatMap(new Func1<Response<DataResponse<AttributesVoucher>>, Observable<AttributesVoucher>>() {
                    @Override
                    public Observable<AttributesVoucher> call(Response<DataResponse<AttributesVoucher>> dataResponseResponse) {
                        return Observable.just(dataResponseResponse.body().getData());
                    }
                });
    }
}
