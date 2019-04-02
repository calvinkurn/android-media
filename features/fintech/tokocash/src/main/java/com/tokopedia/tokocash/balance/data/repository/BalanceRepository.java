package com.tokopedia.tokocash.balance.data.repository;

import com.tokopedia.tokocash.balance.data.datasource.BalanceDataSourceFactory;
import com.tokopedia.tokocash.balance.data.mapper.BalanceTokoCashMapper;
import com.tokopedia.tokocash.balance.domain.IBalanceRepository;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class BalanceRepository implements IBalanceRepository {

    private BalanceDataSourceFactory balanceDataSourceFactory;
    private BalanceTokoCashMapper balanceTokoCashMapper;

    @Inject
    public BalanceRepository(BalanceDataSourceFactory balanceDataSourceFactory,
                             BalanceTokoCashMapper balanceTokoCashMapper) {
        this.balanceDataSourceFactory = balanceDataSourceFactory;
        this.balanceTokoCashMapper = balanceTokoCashMapper;
    }

    @Override
    public Observable<BalanceTokoCash> getBalanceTokoCash() {
        return balanceDataSourceFactory.createBalanceTokoCashDataSource().getBalanceTokoCash()
                .map(balanceTokoCashMapper);
    }

    @Override
    public Observable<BalanceTokoCash> getLocalBalanceTokoCash() {
        return balanceDataSourceFactory.createLocalBalanceTokoCashDataSource().getBalanceTokoCash()
                .map(balanceTokoCashMapper);
    }

}