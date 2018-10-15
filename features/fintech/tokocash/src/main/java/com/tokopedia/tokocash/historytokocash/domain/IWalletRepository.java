package com.tokopedia.tokocash.historytokocash.domain;

import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.tokocash.historytokocash.presentation.model.WithdrawSaldo;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface IWalletRepository {

    Observable<TokoCashHistoryData> getTokoCashHistoryData(HashMap<String, String> mapParams);

    Observable<WithdrawSaldo> withdrawTokoCashToSaldo(String url, HashMap<String, String> mapParams);
}
