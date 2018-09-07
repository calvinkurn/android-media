package com.tokopedia.tokocash.activation.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tokocash.activation.data.entity.ActivateTokoCashEntity;
import com.tokopedia.tokocash.network.api.TokoCashApi;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public class ActivateTokoCashCloudDataStore implements ActivateTokoCashDataStore {

    private TokoCashApi tokoCashApi;

    public ActivateTokoCashCloudDataStore(TokoCashApi tokoCashApi) {
        this.tokoCashApi = tokoCashApi;
    }

    @Override
    public Observable<ActivateTokoCashEntity> requestOTPWallet() {
        return tokoCashApi.requestOtpWallet()
                .map(new Func1<Response<DataResponse<ActivateTokoCashEntity>>, ActivateTokoCashEntity>() {
                    @Override
                    public ActivateTokoCashEntity call(Response<DataResponse<ActivateTokoCashEntity>> dataResponse) {
                        return dataResponse.body().getData();
                    }
                });
    }

    @Override
    public Observable<ActivateTokoCashEntity> linkedWalletToTokoCash(HashMap<String, String> mapParam) {
        return tokoCashApi.linkedWalletToTokocash(mapParam)
                .map(new Func1<Response<DataResponse<ActivateTokoCashEntity>>, ActivateTokoCashEntity>() {
                    @Override
                    public ActivateTokoCashEntity call(Response<DataResponse<ActivateTokoCashEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }
}
