package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.tokopedia.tokocash.historytokocash.data.entity.HelpHistoryTokoCashEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.WithdrawSaldoEntity;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 12/19/17.
 */

public interface WalletDataSource {

    Observable<TokoCashHistoryEntity> getTokoCashHistoryData(HashMap<String, String> mapParams);

    Observable<List<HelpHistoryTokoCashEntity>> getHelpHistoryData();

    Observable<Boolean> submitHelpTokoCash(HashMap<String, String> mapParams);

    Observable<WithdrawSaldoEntity> withdrawTokoCashToSaldo(String url, HashMap<String, String> mapParams);
}
