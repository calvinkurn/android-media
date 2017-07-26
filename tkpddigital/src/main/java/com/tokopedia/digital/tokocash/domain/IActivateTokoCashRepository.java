package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;

import rx.Observable;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public interface IActivateTokoCashRepository {

    Observable<RequestOtpModel> requestOTPWallet();

    Observable<ValidateOtpModel> linkedWalletToTokoCash(String otpCode);
}
