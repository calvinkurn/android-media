package com.tokopedia.loyalty.domain.repository;


import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.network.utils.TKPDMapParam;

import rx.Observable;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface ITokoPointRepository {
    Observable<CouponsDataWrapper> getCouponList(TKPDMapParam<String, String> param);
    Observable<TokoPointDrawerData> getPointDrawer(String requestQuery);
    Observable<VoucherViewModel> checkDigitalVoucherValidity(
            TKPDMapParam<String, String> param, String voucherCode
    );

    Observable<CouponViewModel> checkDigitalCouponValidity(
            TKPDMapParam<String, String> param, String voucherCode, String couponTitle
    );
}
