package com.tokopedia.digital.newcart.domain;

import com.tokopedia.digital.newcart.domain.model.VoucherDigital;

import java.util.Map;

import rx.Observable;

/**
 * @author anggaprasetiyo on 3/6/17.
 */

public interface IVoucherDigitalRepository {

    Observable<VoucherDigital> checkVoucher(Map<String, String> param);

}
