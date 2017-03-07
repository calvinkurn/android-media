package com.tokopedia.digital.cart.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * @author anggaprasetiyo on 3/6/17.
 */

public interface IVoucherDigitalRepository {

    Observable<String> checkVoucher(TKPDMapParam<String, String> param);
}
