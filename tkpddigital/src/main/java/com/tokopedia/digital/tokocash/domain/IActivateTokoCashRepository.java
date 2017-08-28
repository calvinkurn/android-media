package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;

import rx.Observable;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public interface IActivateTokoCashRepository {

    Observable<ActivateTokoCashData> requestOTPWallet();

    Observable<ActivateTokoCashData> linkedWalletToTokoCash(String otpCode);
}
