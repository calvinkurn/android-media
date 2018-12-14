package com.tokopedia.digital.cart.data.repository;

import android.support.annotation.NonNull;

import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.response.voucher.ResponseVoucherData;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.domain.IVoucherDigitalRepository;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 3/6/17.
 */

public class VoucherDigitalRepository implements IVoucherDigitalRepository {

    private final DigitalRestApi digitalRestApi;
    private final ICartMapperData cartMapperData;

    public VoucherDigitalRepository(DigitalRestApi digitalRestApi,
                                    ICartMapperData mapperData) {
        this.digitalRestApi = digitalRestApi;
        this.cartMapperData = mapperData;
    }

    @Override
    public Observable<VoucherDigital> checkVoucher(Map<String, String> param) {
        return digitalRestApi.checkVoucher(param)
                .map(getFuncResponseToVoucherDigital());
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, VoucherDigital> getFuncResponseToVoucherDigital() {
        return tkpdDigitalResponseResponse -> cartMapperData.transformVoucherDigitalData(
                tkpdDigitalResponseResponse.body().convertDataObj(
                        ResponseVoucherData.class
                )
        );
    }
}
