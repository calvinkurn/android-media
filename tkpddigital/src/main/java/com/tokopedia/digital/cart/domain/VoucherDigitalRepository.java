package com.tokopedia.digital.cart.domain;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.cart.data.entity.response.ResponseVoucherData;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.model.VoucherDigital;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 3/6/17.
 */

public class VoucherDigitalRepository implements IVoucherDigitalRepository {

    private final DigitalEndpointService digitalEndpointService;
    private final ICartMapperData cartMapperData;

    public VoucherDigitalRepository(DigitalEndpointService digitalEndpointService,
                                    ICartMapperData mapperData) {
        this.digitalEndpointService = digitalEndpointService;
        this.cartMapperData = mapperData;
    }

    @Override
    public Observable<VoucherDigital> checkVoucher(TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi().checkVoucher(param)
                .map(getFuncResponseToVoucherDigital());
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, VoucherDigital> getFuncResponseToVoucherDigital() {
        return new Func1<Response<TkpdDigitalResponse>, VoucherDigital>() {
            @Override
            public VoucherDigital call(
                    Response<TkpdDigitalResponse> tkpdDigitalResponseResponse
            ) {
                return cartMapperData.transformVoucherDigitalData(
                        tkpdDigitalResponseResponse.body().convertDataObj(
                                ResponseVoucherData.class
                        )
                );
            }
        };
    }
}
