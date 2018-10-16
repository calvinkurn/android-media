package com.tokopedia.sessioncommon.domain.usecase;

import com.tokopedia.sessioncommon.data.SessionCommonApi;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;
import com.tokopedia.sessioncommon.domain.MakeLoginMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginUseCase extends UseCase<MakeLoginPojo> {

    public static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_UUID = "uuid";
    private final SessionCommonApi api;
    private final MakeLoginMapper mapper;
    private UserSession userSession;

    @Inject
    public MakeLoginUseCase(SessionCommonApi api,
                            UserSession userSession,
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

    public static RequestParams getParam(String userId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        return params;
    }

    private Action1<MakeLoginPojo> saveToCache() {
        return makeLoginDomain -> {
            if (makeLoginDomain.getIsLogin().equals("1")) {
                userSession.setLoginSession(makeLoginDomain.getIsLogin().equals("1"),
                        String.valueOf(makeLoginDomain.getUserId()),
                        makeLoginDomain.getFullName(),
                        String.valueOf(makeLoginDomain.getShopId()),
                        makeLoginDomain.getMsisdnIsVerified().equals("1"),
                        makeLoginDomain.getShopName(),
                        userSession.getTempEmail(),
                        makeLoginDomain.getShopIsGold(),
                        userSession.getTempPhoneNumber());
            } else {
                userSession.setTempLoginName(makeLoginDomain.getFullName());
                userSession.setTempLoginSession(String.valueOf(makeLoginDomain.getUserId()));
                userSession.setIsMSISDNVerified(makeLoginDomain.getMsisdnIsVerified().equals("1"));
            }
        };
    }
}
