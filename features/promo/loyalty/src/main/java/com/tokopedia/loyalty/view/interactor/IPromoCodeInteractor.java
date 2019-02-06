package com.tokopedia.loyalty.view.interactor;

import com.tokopedia.loyalty.view.data.VoucherViewModel;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface IPromoCodeInteractor {

    void submitDigitalVoucher(
            String voucherCode,
            Map<String, String> param, Subscriber<VoucherViewModel> subscriber);


}
