package com.tokopedia.forgotpassword.domain;

import com.tokopedia.forgotpassword.data.ForgotPasswordApi;
import com.tokopedia.forgotpassword.data.pojo.ResetPasswordPojo;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 9/25/18.
 */
public class ResetPasswordUseCase extends UseCase<ResetPasswordPojo> {

    private final ForgotPasswordApi api;
    private final ResetPasswordMapper mapper;

    @Inject
    public ResetPasswordUseCase(ForgotPasswordApi api,
                                ResetPasswordMapper mapper) {
        this.api = api;
        this.mapper = mapper;
    }

    @Override
    public Observable<ResetPasswordPojo> createObservable(RequestParams requestParams) {
        return api.resetPassword(requestParams.getParameters())
                .map(mapper);
    }

    public static RequestParams getParam(String email, String deviceId, String userId) {
        RequestParams params = RequestParams.create();

        String hash = AuthUtil.md5(userId + "~" + deviceId);
        params.putString("email", email);
        params.putString("device_id", deviceId);
        params.putString("hash", hash);
        params.putString("os_type", "1");
        params.putString("device_time", String.valueOf((new Date().getTime()) / 1000));
        return params;
    }
}




