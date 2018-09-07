package com.tokopedia.tokocash.pendingcashback.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tokocash.activation.data.entity.PendingCashbackEntity;
import com.tokopedia.tokocash.network.api.WalletApi;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public class PendingCashbackCloudDataStore implements PendingCashbackDataStore {

    private WalletApi walletApi;

    public PendingCashbackCloudDataStore(WalletApi walletApi) {
        this.walletApi = walletApi;
    }

    @Override
    public Observable<PendingCashbackEntity> getPendingCashback(HashMap<String, String> mapParam) {
        return walletApi.getTokoCashPending(mapParam)
                .map(new Func1<Response<DataResponse<PendingCashbackEntity>>, PendingCashbackEntity>() {
                    @Override
                    public PendingCashbackEntity call(Response<DataResponse<PendingCashbackEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }
}
