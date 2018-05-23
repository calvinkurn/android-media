package com.tokopedia.digital.cart.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.model.OtpData;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public interface IOtpVerificationInteractor {
    void requestOtp(TKPDMapParam<String, Object> parameters, Subscriber<OtpData> subscriber);

    void verifyOtp(TKPDMapParam<String, Object> parameters, Subscriber<OtpData> subscriber);
}
