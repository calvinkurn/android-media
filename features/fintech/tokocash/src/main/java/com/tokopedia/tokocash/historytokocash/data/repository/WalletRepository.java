package com.tokopedia.tokocash.historytokocash.data.repository;

import com.tokopedia.tokocash.historytokocash.data.datasource.WalletDataSourceFactory;
import com.tokopedia.tokocash.historytokocash.data.mapper.TokoCashHistoryMapper;
import com.tokopedia.tokocash.historytokocash.data.mapper.WithdrawSaldoMapper;
import com.tokopedia.tokocash.historytokocash.domain.IWalletRepository;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.tokocash.historytokocash.presentation.model.WithdrawSaldo;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public class WalletRepository implements IWalletRepository {

    private final WalletDataSourceFactory walletDataSourceFactory;
    private TokoCashHistoryMapper tokoCashHistoryMapper;
    private WithdrawSaldoMapper withdrawSaldoMapper;

    @Inject
    public WalletRepository(WalletDataSourceFactory walletDataSourceFactory,
                            TokoCashHistoryMapper tokoCashHistoryMapper,
                            WithdrawSaldoMapper withdrawSaldoMapper) {
        this.walletDataSourceFactory = walletDataSourceFactory;
        this.tokoCashHistoryMapper = tokoCashHistoryMapper;
        this.withdrawSaldoMapper = withdrawSaldoMapper;
    }

    @Override
    public Observable<TokoCashHistoryData> getTokoCashHistoryData(HashMap<String, String> mapParams) {
        return walletDataSourceFactory.create()
                .getTokoCashHistoryData(mapParams)
                .map(tokoCashHistoryMapper);
    }

    @Override
    public Observable<WithdrawSaldo> withdrawTokoCashToSaldo(String url, HashMap<String, String> mapParams) {
        return walletDataSourceFactory.create().withdrawTokoCashToSaldo(url, mapParams)
                .map(withdrawSaldoMapper);
    }
}
