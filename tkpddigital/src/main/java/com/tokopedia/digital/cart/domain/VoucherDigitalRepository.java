package com.tokopedia.digital.cart.domain;

import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 3/6/17.
 */

public class VoucherDigitalRepository implements IVoucherDigitalRepository {

    private final DigitalEndpointService digitalEndpointService;

    public VoucherDigitalRepository() {
        this.digitalEndpointService = new DigitalEndpointService();
    }

    @Override
    public Observable<String> checkVoucher(TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi().checkVoucher(param)
                .map(new Func1<Response<TkpdDigitalResponse>, String>() {
                    @Override
                    public String call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tkpdDigitalResponseResponse.body().getStrResponse();
                    }
                });
    }
}
