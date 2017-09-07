package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.digital.tokocash.entity.TokoCashHistoryEntity;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;

import rx.Observable;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public interface ITokoCashRepository {

    Observable<ActivateTokoCashData> requestOTPWallet();

    Observable<ActivateTokoCashData> linkedWalletToTokoCash(String otpCode);

    Observable<TokoCashHistoryEntity> getTokoCashHistoryData(String type, String startDate,
                                                             String endDate, String afterId);

    Observable<TokoCashData> getBalanceTokoCash();
}
