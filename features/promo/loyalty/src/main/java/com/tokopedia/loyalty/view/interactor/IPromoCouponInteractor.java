package com.tokopedia.loyalty.view.interactor;

import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;

import java.util.Map;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCouponInteractor {
    void getCouponList(Map<String, String> param, Subscriber<CouponsDataWrapper> subscriber);

    void submitDigitalVoucher(String couponTitle,
                              String voucherCode,
                              Map<String, String> param,
                              Subscriber<CouponViewModel> subscriber);

    void unsubscribe();

}
