package com.tokopedia.digital.cart.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;

import java.util.Map;

import rx.Observable;

/**
 * @author anggaprasetiyo on 3/6/17.
 */

public interface IVoucherDigitalRepository {

    Observable<VoucherDigital> checkVoucher(Map<String, String> param);

}
