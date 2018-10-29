package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tokocash.historytokocash.data.entity.TokoCashHistoryEntity;
import com.tokopedia.tokocash.historytokocash.data.entity.WithdrawSaldoEntity;
import com.tokopedia.tokocash.network.api.WalletApi;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class CloudWalletDataSource implements WalletDataSource {

    private WalletApi walletApi;
    private Gson gson;

    public CloudWalletDataSource(WalletApi walletApi, Gson gson) {
        this.walletApi = walletApi;
        this.gson = gson;
    }

    @Override
    public Observable<TokoCashHistoryEntity> getTokoCashHistoryData(HashMap<String, String> mapParams) {
        return walletApi.getHistoryTokocash(mapParams)
                .map(new Func1<Response<DataResponse<TokoCashHistoryEntity>>, TokoCashHistoryEntity>() {
                    @Override
                    public TokoCashHistoryEntity call(Response<DataResponse<TokoCashHistoryEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }

    @Override
    public Observable<WithdrawSaldoEntity> withdrawTokoCashToSaldo(String url, HashMap<String, String> mapParams) {
        return walletApi.withdrawSaldoFromTokocash(url, mapParams)
                .map(new Func1<Response<DataResponse<WithdrawSaldoEntity>>, WithdrawSaldoEntity>() {
                    @Override
                    public WithdrawSaldoEntity call(Response<DataResponse<WithdrawSaldoEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }
}