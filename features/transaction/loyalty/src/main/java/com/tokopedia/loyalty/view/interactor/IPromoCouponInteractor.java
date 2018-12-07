package com.tokopedia.loyalty.view.interactor;

import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.network.utils.TKPDMapParam;

import java.util.Map;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCouponInteractor {
    void getCouponList(TKPDMapParam<String, String> param, Subscriber<CouponsDataWrapper> subscriber);

    void submitDigitalVoucher(String couponTitle,
                              String voucherCode,
                              TKPDMapParam<String, String> param,
                              Subscriber<CouponViewModel> subscriber);

    void unsubscribe();

}
