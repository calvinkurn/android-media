package com.tokopedia.loginregister.loginthirdparty.domain;

import com.facebook.AccessToken;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;
import com.tokopedia.sessioncommon.data.model.TokenViewModel;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.domain.usecase.GetTokenUseCase;
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoUseCase;
import com.tokopedia.sessioncommon.domain.usecase.MakeLoginUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/11/17.
 */
@Deprecated
public class LoginWithSosmedUseCase extends UseCase<LoginEmailDomain> {

    protected final GetTokenUseCase getTokenUseCase;
    protected final GetUserInfoUseCase getUserInfoUseCase;
    protected final MakeLoginUseCase makeLoginUseCase;
    private final UserSessionInterface userSessionInterface;

    @Inject
    public LoginWithSosmedUseCase(GetTokenUseCase getTokenUseCase,
                                  GetUserInfoUseCase getUserInfoUseCase,
                                  MakeLoginUseCase makeLoginUseCase,
                                  @Named(SessionModule.SESSION_MODULE)
                                              UserSessionInterface userSessionInterface) {
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
        this.userSessionInterface = userSessionInterface;
    }

    @Override
    public Observable<LoginEmailDomain> createObservable(final RequestParams requestParams) {
        LoginEmailDomain registerSosmedDomain = new LoginEmailDomain();
        return getToken(registerSosmedDomain,
                GetTokenUseCase.getParamThirdParty(
                        requestParams.getInt(GetTokenUseCase.SOCIAL_TYPE, -1),
                        requestParams.getString(GetTokenUseCase.ACCESS_TOKEN, "")
                ))
                .flatMap((Func1<LoginEmailDomain, Observable<LoginEmailDomain>>) this::getInfo)
                .flatMap((Func1<LoginEmailDomain, Observable<LoginEmailDomain>>) this::makeLogin);
    }

    protected Observable<LoginEmailDomain> makeLogin(final LoginEmailDomain
                                                              registerSosmedDomain) {
        return makeLoginUseCase.getExecuteObservable(MakeLoginUseCase.getParam(
                String.valueOf(registerSosmedDomain.getInfo().getUserId()),
                userSessionInterface.getDeviceId())
        )
                .flatMap((Func1<MakeLoginPojo, Observable<LoginEmailDomain>>) makeLoginPojo -> {
                    registerSosmedDomain.setLoginResult(makeLoginPojo);
                    return Observable.just(registerSosmedDomain);
                });
    }

    protected Observable<LoginEmailDomain> getInfo(final LoginEmailDomain
                                                            registerSosmedDomain) {
        return getUserInfoUseCase.createObservable(RequestParams.EMPTY)
                .flatMap((Func1<GetUserInfoData, Observable<LoginEmailDomain>>) userInfoData -> {
                    registerSosmedDomain.setInfo(userInfoData);
                    return Observable.just(registerSosmedDomain);
                });
    }

    protected Observable<LoginEmailDomain> getToken(final LoginEmailDomain registerSosmedDomain,
                                                     RequestParams params) {
        return getTokenUseCase.createObservable(params)
                .flatMap((Func1<TokenViewModel, Observable<LoginEmailDomain>>) tokenViewModel -> {
                    registerSosmedDomain.setToken(tokenViewModel);
                    return Observable.just(registerSosmedDomain);
                });
    }


    private static RequestParams getParamThirdParty(int socialType, String accessToken) {
        return GetTokenUseCase.getParamThirdParty(
                socialType,
                accessToken
        );
    }

    public static RequestParams getParamFacebook(AccessToken accessToken) {
        RequestParams params = RequestParams.create();
        params.putAll(
                getParamThirdParty(GetTokenUseCase.SOCIAL_TYPE_FACEBOOK,
                        accessToken.getToken())
                        .getParameters());
        return params;
    }

    public static RequestParams getParamGoogle(String accessToken) {
        RequestParams params = RequestParams.create();
        params.putAll(
                getParamThirdParty(GetTokenUseCase.SOCIAL_TYPE_GPLUS,
                        accessToken)
                        .getParameters());
        return params;
    }


}
