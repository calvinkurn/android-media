package com.tokopedia.forgotpassword.domain;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.forgotpassword.data.ForgotPasswordApi;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordRetrofitInteractorImpl implements ForgotPasswordRetrofitInteractor {

    private final ForgotPasswordApi forgotPasswordApi;

    public ForgotPasswordRetrofitInteractorImpl(ForgotPasswordApi forgotPasswordApi) {
        this.forgotPasswordApi = forgotPasswordApi;
    }

    @Override
    public Observable<Response<TkpdResponse>> resetPassword(TKPDMapParam<String, String> param) {
        return forgotPasswordApi.resetPassword(
                AuthUtil.generateParamsNetwork(
                        MainApplication.getAppContext(),
                        param));
    }
}
