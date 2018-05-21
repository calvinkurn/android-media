package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.digital.tokocash.entity.WalletTokenEntity;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;

import rx.Observable;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public interface ITokoCashRepository {

    Observable<ActivateTokoCashData> requestOTPWallet();

    Observable<ActivateTokoCashData> linkedWalletToTokoCash(String otpCode);

    Observable<TokoCashBalanceData> getBalanceTokoCash();

    Observable<WalletTokenEntity> getWalletToken();
}
