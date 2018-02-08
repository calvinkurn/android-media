package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public interface IActivateTokoCashInteractor {

    void requestOTPWallet(Subscriber<ActivateTokoCashData> subscriber);

    void activateTokoCash(String otpCode, Subscriber<ActivateTokoCashData> subscriber);
}
