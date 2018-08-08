package com.tokopedia.tokocash.accountsetting.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tokocash.accountsetting.data.entity.OAuthInfoEntity;
import com.tokopedia.tokocash.accountsetting.data.entity.RevokeTokoCashEntity;
import com.tokopedia.tokocash.network.api.WalletApi;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public class AccountSettingCloudDataStore implements AccountSettingDataStore {

    private WalletApi walletApi;

    public AccountSettingCloudDataStore(WalletApi walletApi) {
        this.walletApi = walletApi;
    }

    @Override
    public Observable<OAuthInfoEntity> getOAuthInfo() {
        return walletApi.getOAuthInfoAccount()
                .map(new Func1<Response<DataResponse<OAuthInfoEntity>>, OAuthInfoEntity>() {
                    @Override
                    public OAuthInfoEntity call(Response<DataResponse<OAuthInfoEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }

    @Override
    public Observable<Boolean> unlinkAccountTokoCash(HashMap<String, String> mapParam) {
        return walletApi.revokeAccessAccountTokoCash(mapParam)
                .map(new Func1<Response<RevokeTokoCashEntity>, Boolean>() {
                    @Override
                    public Boolean call(Response<RevokeTokoCashEntity> revokeTokoCashEntityResponse) {
                        return revokeTokoCashEntityResponse.isSuccessful();
                    }
                });
    }
}
