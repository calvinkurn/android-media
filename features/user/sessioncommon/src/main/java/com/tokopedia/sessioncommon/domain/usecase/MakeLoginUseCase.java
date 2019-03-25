package com.tokopedia.sessioncommon.domain.usecase;

import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.sessioncommon.data.MakeLoginApi;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.domain.mapper.MakeLoginMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginUseCase extends UseCase<MakeLoginPojo> {

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_UUID = "uuid";
    public static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    private static final String TYPE_ANDROID = "1";

    private final MakeLoginApi api;
    private final MakeLoginMapper mapper;
    private UserSessionInterface userSession;

    @Inject
    public MakeLoginUseCase(MakeLoginApi api,
                            @Named(SessionModule.SESSION_MODULE) UserSessionInterface userSession,
                            MakeLoginMapper mapper) {
        this.api = api;
        this.userSession = userSession;
        this.mapper = mapper;
    }

    @Override
    public Observable<MakeLoginPojo> createObservable(RequestParams requestParams) {
        return api.makeLogin(requestParams.getParameters())
                .map(mapper)
                .doOnNext(saveToCache());
    }

    public static RequestParams getParam(String userId, String deviceId) {
        String hash = AuthUtil.md5(userId + "~" + deviceId);

        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_DEVICE_ID, deviceId);
        params.putString(PARAM_HASH, hash);
        params.putString(PARAM_OS_TYPE, TYPE_ANDROID);
        params.putString(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return params;
    }

    private Action1<MakeLoginPojo> saveToCache() {
        return makeLoginDomain -> {
            if (makeLoginDomain.getSecurityPojo().getAllowLogin() == 1) {
                userSession.setLoginSession(true,
                        String.valueOf(makeLoginDomain.getUserId()),
                        makeLoginDomain.getFullName(),
                        String.valueOf(makeLoginDomain.getShopId()),
                        makeLoginDomain.getMsisdnIsVerified().equals("1"),
                        makeLoginDomain.getShopName(),
                        userSession.getTempEmail(),
                        makeLoginDomain.getShopIsGold() == 1,
                        userSession.getTempPhoneNumber());
                userSession.setShopAvatar(makeLoginDomain.getShopAvatar());
            } else {
                userSession.setTempLoginName(makeLoginDomain.getFullName());
                userSession.setTempUserId(String.valueOf(makeLoginDomain.getUserId()));
                userSession.setIsMSISDNVerified(makeLoginDomain.getMsisdnIsVerified().equals("1"));
            }
        };
    }
}
