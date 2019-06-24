package com.tokopedia.otp.cotp.domain.interactor;

import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.otp.cotp.domain.source.MakeLoginDataSource;
import com.tokopedia.otp.cotp.view.viewmodel.OtpLoginDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

@Deprecated
public class MakeLoginUseCase extends UseCase<OtpLoginDomain> {

    static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    private static final String PARAM_UUID = "uuid";
    private static final String TYPE_ANDROID = "1";

    private final MakeLoginDataSource makeLoginDataSource;

    @Inject
    public MakeLoginUseCase(MakeLoginDataSource makeLoginDataSource) {
        this.makeLoginDataSource = makeLoginDataSource;
    }

    @Override
    public Observable<OtpLoginDomain> createObservable(RequestParams requestParams) {
        return makeLoginDataSource.makeLogin(requestParams.getParameters());
    }

    public static RequestParams getParam(String userId, String deviceId) {
        String hash = AuthUtil.md5(userId + "~" + deviceId);

        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_DEVICE_ID, deviceId);
        params.putString(PARAM_HASH, hash);
        params.putString(PARAM_OS_TYPE, TYPE_ANDROID);
        params.putString(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return params;
    }

}
