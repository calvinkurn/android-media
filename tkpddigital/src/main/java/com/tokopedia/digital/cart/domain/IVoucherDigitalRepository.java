package com.tokopedia.digital.cart.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.model.VoucherDigital;

import rx.Observable;

/**
 * @author anggaprasetiyo on 3/6/17.
 */

public interface IVoucherDigitalRepository {

    Observable<VoucherDigital> checkVoucher( TKPDMapParam<String, String> param);
}
