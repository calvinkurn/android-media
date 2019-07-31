package com.tokopedia.sessioncommon.domain.usecase;

import com.tokopedia.sessioncommon.data.GetProfileApi;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.domain.mapper.GetUserInfoMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/16/18.
 */
@Deprecated
public class GetUserInfoUseCase extends UseCase<GetUserInfoData> {

    private final GetProfileApi sessionCommonApi;
    private final GetUserInfoMapper mapper;
    private final UserSessionInterface userSession;

    @Inject
    public GetUserInfoUseCase(GetProfileApi sessionCommonApi,
                              GetUserInfoMapper mapper,
                              @Named(SessionModule.SESSION_MODULE)
                                          UserSessionInterface userSession) {
        this.sessionCommonApi = sessionCommonApi;
        this.mapper = mapper;
        this.userSession = userSession;

    }

    @Override
    public Observable<GetUserInfoData> createObservable(RequestParams requestParams) {
        return sessionCommonApi.getInfo(requestParams.getParameters())
                .map(mapper)
                .doOnNext(saveToCache());
    }

    private Action1<? super GetUserInfoData> saveToCache() {
        return (Action1<GetUserInfoData>) getUserInfoData -> {
            if (!userSession.isLoggedIn()) {
                userSession.setTempUserId(String.valueOf(getUserInfoData.getUserId()));
                userSession.setTempPhoneNumber(getUserInfoData.getPhone());
                userSession.setTempLoginName(getUserInfoData.getFullName());
                userSession.setTempLoginEmail(getUserInfoData.getEmail());
            }
            userSession.setHasPassword(getUserInfoData.isCreatedPassword());
            userSession.setProfilePicture(getUserInfoData.getProfilePicture());
            userSession.setIsMSISDNVerified(getUserInfoData.isPhoneVerified());

        };
    }

    public static RequestParams generateParam() {
        return RequestParams.EMPTY;
    }
}
