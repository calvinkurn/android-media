package com.tokopedia.digital.newcart.data.repository;

import androidx.annotation.NonNull;

import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;
import com.tokopedia.digital.newcart.data.entity.response.voucher.ResponseVoucherData;
import com.tokopedia.digital.newcart.domain.IVoucherDigitalRepository;
import com.tokopedia.digital.newcart.domain.mapper.ICartMapperData;
import com.tokopedia.digital.newcart.domain.model.VoucherDigital;

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
