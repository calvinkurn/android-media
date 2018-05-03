package com.tokopedia.tokocash.qrpayment.data.datasource;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.network.api.WalletBalanceApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class CloudBalanceDataSource implements BalanceDataSource {

    private static final String TAG = CloudBalanceDataSource.class.getName();
    private static final int DURATION_SAVE_TO_CACHE = 60;

    private WalletBalanceApi walletApi;
    private CacheManager cacheManager;


    private Context context;

    public CloudBalanceDataSource(WalletBalanceApi walletApi, CacheManager cacheManager, Context context) {
        this.walletApi = walletApi;
        this.cacheManager = cacheManager;
        this.context = context;
    }

    @Override
    public Observable<Wallet> getBalanceTokoCash() {
        return walletApi.getBalance(getRequestPayload())
                .doOnNext(new Action1<Response<GraphqlResponse<UserData>>>() {
                    @Override
                    public void call(Response<GraphqlResponse<UserData>> dataResponseResponse) {
                        if (dataResponseResponse.body().getData() != null && dataResponseResponse.body().getData().getWallet() != null && dataResponseResponse.body().getData().getWallet().getLinked()) {
                            cacheManager.save(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE,
                                    CacheUtil.convertModelToString(dataResponseResponse.body().getData().getWallet(),
                                            new TypeToken<Wallet>() {
                                            }.getType()), DURATION_SAVE_TO_CACHE);
                        }
                    }
                })
                .map(new Func1<Response<GraphqlResponse<UserData>>, Wallet>() {
                    @Override
                    public Wallet call(Response<GraphqlResponse<UserData>> userData) {
                        return userData.body().getData().getWallet();
                    }
                });
    }

    private String getRequestPayload() {
        if (context == null) return "";
        return loadRawString(context.getResources(), R.raw.wallet_balance_query);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }
}