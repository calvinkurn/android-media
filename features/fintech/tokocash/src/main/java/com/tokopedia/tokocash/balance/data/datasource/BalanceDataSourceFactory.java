package com.tokopedia.tokocash.balance.data.datasource;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.network.api.WalletBalanceApi;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class BalanceDataSourceFactory {

    private WalletBalanceApi walletApi;
    private CacheManager cacheManager;
    private Context context;

    @Inject
    public BalanceDataSourceFactory(WalletBalanceApi walletApi, CacheManager cacheManager, Context context) {
        this.walletApi = walletApi;
        this.cacheManager = cacheManager;
        this.context = context;
    }

    public BalanceDataSource createBalanceTokoCashDataSource() {
        return new CloudBalanceDataSource(walletApi, context);
    }

    public BalanceDataSource createLocalBalanceTokoCashDataSource() {
        return new LocalBalanceDataSource();
    }
}