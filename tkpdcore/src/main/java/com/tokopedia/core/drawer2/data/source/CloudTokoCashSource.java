package com.tokopedia.core.drawer2.data.source;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 5/5/17.
 */

public class CloudTokoCashSource {

    private final Context context;
    private final AccountsService accountsService;
    private final TokoCashMapper tokoCashMapper;
    private final GlobalCacheManager walletCache;

    public CloudTokoCashSource(Context context,
                               AccountsService accountsService,
                               TokoCashMapper tokoCashMapper,
                               GlobalCacheManager walletCache) {
        this.context = context;
        this.accountsService = accountsService;
        this.tokoCashMapper = tokoCashMapper;
        this.walletCache = walletCache;
    }

    public Observable<TokoCashModel> getTokoCash(TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .getTokoCash(AuthUtil.generateParamsNetwork2(context, params))
                .map(tokoCashMapper)
                .doOnNext(saveToCache());
    }

    private Action1<TokoCashModel> saveToCache() {
        return new Action1<TokoCashModel>() {
            @Override
            public void call(TokoCashModel tokoCashModel) {
                if (tokoCashModel != null && tokoCashModel.isSuccess()) {
                    walletCache.setKey(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);
                    walletCache.setValue(CacheUtil.convertModelToString(tokoCashModel,
                            new TypeToken<TokoCashModel>() {
                            }.getType()));
                    walletCache.setCacheDuration(60);
                    walletCache.store();
                }
            }
        };
    }
}
