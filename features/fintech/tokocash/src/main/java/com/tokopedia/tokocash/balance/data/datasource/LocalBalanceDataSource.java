package com.tokopedia.tokocash.balance.data.datasource;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.balance.data.entity.BalanceTokoCashEntity;
import com.tokopedia.cachemanager.PersistentCacheManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class LocalBalanceDataSource implements BalanceDataSource {

    private static final String TAG = LocalBalanceDataSource.class.getName();

    public LocalBalanceDataSource() {
    }

    @Override
    public Observable<BalanceTokoCashEntity> getBalanceTokoCash() {
        return Observable.just(true).map(new Func1<Boolean, BalanceTokoCashEntity>() {
            @Override
            public BalanceTokoCashEntity call(Boolean aBoolean) {
                if (getCache() != null) {
                    return (CacheUtil.convertStringToModel(getCache(), new TypeToken<BalanceTokoCashEntity>() {
                    }.getType()));
                } else
                    throw new RuntimeException("Cache has expired");
            }
        }).map(new Func1<BalanceTokoCashEntity, BalanceTokoCashEntity>() {
            @Override
            public BalanceTokoCashEntity call(BalanceTokoCashEntity wallet) {
                return wallet;
            }
        });
    }

    private String getCache() {
        return PersistentCacheManager.instance.getString(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }

}