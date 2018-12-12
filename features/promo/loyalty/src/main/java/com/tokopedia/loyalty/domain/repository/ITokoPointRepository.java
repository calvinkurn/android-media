package com.tokopedia.loyalty.domain.repository;


import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import java.util.Map;

import rx.Observable;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface ITokoPointRepository {
    Observable<CouponsDataWrapper> getCouponList(Map<String, String> param);
    Observable<TokoPointDrawerData> getPointDrawer(String requestQuery);
    Observable<VoucherViewModel> checkDigitalVoucherValidity(
            Map<String, String> param, String voucherCode
    );

    Observable<CouponViewModel> checkDigitalCouponValidity(
            Map<String, String> param, String voucherCode, String couponTitle
    );
}
