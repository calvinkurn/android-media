package com.tokopedia.core.drawer2.data.source;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.var.TkpdCache;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class LocalTokoCashSource {

    private final GlobalCacheManager walletCache;

    public LocalTokoCashSource(GlobalCacheManager walletCache) {
        this.walletCache = walletCache;
    }

    public Observable<TokoCashModel> getTokoCash() {

        return Observable.just(true).map(new Func1<Boolean, TokoCashModel>() {
            @Override
            public TokoCashModel call(Boolean aBoolean) {

                if (getCache() != null) {
                    return CacheUtil.convertStringToModel(getCache(),
                            new TypeToken<TokoCashModel>() {
                            }.getType());
                } else
                    throw new RuntimeException("Cache has expired");

            }
        });
    }

    private String getCache() {
        return walletCache.getValueString(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);
    }
}
