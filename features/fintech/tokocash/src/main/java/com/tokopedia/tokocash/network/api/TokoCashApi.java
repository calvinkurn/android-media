package com.tokopedia.tokocash.network.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tokocash.activation.data.entity.ActivateTokoCashEntity;
import com.tokopedia.tokocash.network.model.RefreshTokenEntity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 1/5/17. Tokopedia
 */

public interface TokoCashApi {

    @GET(WalletUrl.Account.PATH_REQUEST_OTP_WALLET)
    Observable<Response<DataResponse<ActivateTokoCashEntity>>> requestOtpWallet();

    @GET(WalletUrl.Account.PATH_LINK_WALLET_TO_TOKOCASH)
    Observable<Response<DataResponse<ActivateTokoCashEntity>>> linkedWalletToTokocash(@QueryMap HashMap<String, String> params);

    @GET(WalletUrl.Account.GET_TOKEN_WALLET)
    Call<RefreshTokenEntity> getTokenWalletSynchronous();
}
