package com.tokopedia.sessioncommon.domain.usecase;

import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;
import com.tokopedia.sessioncommon.data.model.TokenViewModel;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/12/18.
 */
@Deprecated
public class LoginEmailUseCase extends UseCase<LoginEmailDomain> {

    private final GetTokenUseCase getTokenUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;
    private final MakeLoginUseCase makeLoginUseCase;
    private final UserSessionInterface userSessionInterface;

    @Inject
    public LoginEmailUseCase(GetTokenUseCase getTokenUseCase,
                             GetUserInfoUseCase getUserInfoUseCase,
                             MakeLoginUseCase makeLoginUseCase,
                             @Named(SessionModule.SESSION_MODULE) UserSessionInterface userSessionInterface) {
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
        this.userSessionInterface = userSessionInterface;
    }

    @Override
    public Observable<LoginEmailDomain> createObservable(RequestParams requestParams) {
        LoginEmailDomain domain = new LoginEmailDomain();
        return getToken(domain, requestParams)
                .flatMap(getInfo(domain))
                .flatMap(makeLogin(domain));
    }

    private Func1<LoginEmailDomain, Observable<LoginEmailDomain>> makeLogin(final LoginEmailDomain domain) {
        return loginEmailDomain -> makeLoginUseCase.createObservable(
                MakeLoginUseCase.getParam(String.valueOf(loginEmailDomain.getInfo().getUserId()),
                        userSessionInterface.getDeviceId()))
                .flatMap((Func1<MakeLoginPojo, Observable<LoginEmailDomain>>) makeLoginPojo -> {
                    domain.setLoginResult(makeLoginPojo);
                    return Observable.just(domain);
                });
    }

    private Func1<LoginEmailDomain, Observable<LoginEmailDomain>> getInfo(final LoginEmailDomain domain) {
        return loginEmailDomain ->
                getUserInfoUseCase.createObservable(GetUserInfoUseCase.generateParam())
                        .flatMap((Func1<GetUserInfoData, Observable<LoginEmailDomain>>)
                                getUserInfoData -> {
                                    domain.setInfo(getUserInfoData);
                                    return Observable.just(domain);
                                });
    }

    private Observable<LoginEmailDomain> getToken(final LoginEmailDomain domain, RequestParams requestParams) {
        return getTokenUseCase.createObservable(getTokenParam(requestParams))
                .flatMap((Func1<TokenViewModel, Observable<LoginEmailDomain>>) tokenViewModel -> {
                    domain.setToken(tokenViewModel);
                    return Observable.just(domain);
                });
    }

    private RequestParams getTokenParam(RequestParams requestParams) {
        return GetTokenUseCase.getParamLogin(
                requestParams.getString(GetTokenUseCase.USER_NAME, ""),
                requestParams.getString(GetTokenUseCase.PASSWORD, ""));
    }

    public static RequestParams getParam(String email, String password) {
        RequestParams params = RequestParams.create();
        params.putAll(GetTokenUseCase.getParamLogin(email, password).getParameters());
        return params;
    }
}
