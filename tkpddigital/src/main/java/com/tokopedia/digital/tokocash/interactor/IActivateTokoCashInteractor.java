package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public interface IActivateTokoCashInteractor {

    void requestOTPWallet(Subscriber<RequestOtpModel> subscriber);

    void activateTokoCash(String otpCode, Subscriber<ValidateOtpModel> subscriber);

    void onDestroy();
}
