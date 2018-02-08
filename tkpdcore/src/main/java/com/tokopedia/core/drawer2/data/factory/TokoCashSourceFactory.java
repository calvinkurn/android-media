package com.tokopedia.core.drawer2.data.factory;

import android.content.Context;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.source.CloudTokoCashSource;
import com.tokopedia.core.drawer2.data.source.LocalTokoCashSource;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashSourceFactory {

    private Context context;
    private AccountsService accountsService;
    private TokoCashMapper tokoCashMapper;
    private GlobalCacheManager walletCache;

    public TokoCashSourceFactory(Context context,
                AccountsService accountsService,
                                 TokoCashMapper tokoCashMapper,
                                 GlobalCacheManager walletCache) {
        this.context = context;
        this.accountsService = accountsService;
        this.tokoCashMapper = tokoCashMapper;
        this.walletCache = walletCache;
    }

    public CloudTokoCashSource createCloudTokoCashSource() {
        return new CloudTokoCashSource(context, accountsService,
                tokoCashMapper, walletCache);
    }

    public LocalTokoCashSource createLocalTokoCashSource() {
        return new LocalTokoCashSource(walletCache);
    }
}
