package com.tokopedia.tokocash.balance.data.datasource;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.balance.data.entity.BalanceTokoCashEntity;
import com.tokopedia.tokocash.balance.data.entity.BalanceWalletEntity;
import com.tokopedia.tokocash.network.api.WalletBalanceApi;
import com.tokopedia.cachemanager.PersistentCacheManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class CloudBalanceDataSource implements BalanceDataSource {

    private static final String TAG = CloudBalanceDataSource.class.getName();
    private static final String QUERY = "query";
    private static final int DURATION_SAVE_TO_CACHE = 60_000;

    private WalletBalanceApi walletApi;


    private Context context;

    public CloudBalanceDataSource(WalletBalanceApi walletApi, Context context) {
        this.walletApi = walletApi;
        this.context = context;
    }

    @Override
    public Observable<BalanceTokoCashEntity> getBalanceTokoCash() {
        Map<String, Object> requestQuery = new HashMap<>();
        requestQuery.put(QUERY, getRequestPayload());
        return walletApi.getBalance(requestQuery)
                .doOnNext(new Action1<Response<GraphqlResponse<BalanceWalletEntity>>>() {
                    @Override
                    public void call(Response<GraphqlResponse<BalanceWalletEntity>> dataResponseResponse) {
                        if (dataResponseResponse.body().getData() != null && dataResponseResponse.body().getData().getWallet() != null && dataResponseResponse.body().getData().getWallet().getLinked()) {
                            PersistentCacheManager.instance.put(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE,
                                    CacheUtil.convertModelToString(dataResponseResponse.body().getData().getWallet(),
                                            new TypeToken<BalanceTokoCashEntity>() {
                                            }.getType()), DURATION_SAVE_TO_CACHE);
                        }
                    }
                })
                .map(new Func1<Response<GraphqlResponse<BalanceWalletEntity>>, BalanceTokoCashEntity>() {
                    @Override
                    public BalanceTokoCashEntity call(Response<GraphqlResponse<BalanceWalletEntity>> graphqlResponseResponse) {
                        return graphqlResponseResponse.body().getData().getWallet();
                    }
                });
    }

    private String getRequestPayload() {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.wallet_balance_query);
    }
}