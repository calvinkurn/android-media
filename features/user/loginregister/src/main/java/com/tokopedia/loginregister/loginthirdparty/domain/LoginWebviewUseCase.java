package com.tokopedia.loginregister.loginthirdparty.domain;


import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.domain.usecase.GetTokenUseCase;
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoUseCase;
import com.tokopedia.sessioncommon.domain.usecase.MakeLoginUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/16/17.
 */

public class LoginWebviewUseCase extends LoginWithSosmedUseCase {

    @Inject
    public LoginWebviewUseCase(GetTokenUseCase getTokenUseCase,
                               GetUserInfoUseCase getUserInfoUseCase,
                               MakeLoginUseCase makeLoginUseCase,
                               @Named(SessionModule.SESSION_MODULE)
                                           UserSessionInterface userSessionInterface) {
        super(getTokenUseCase, getUserInfoUseCase, makeLoginUseCase, userSessionInterface);
    }

    @Override
    public Observable<LoginEmailDomain> createObservable(final RequestParams requestParams) {
        LoginEmailDomain registerSosmedDomain = new LoginEmailDomain();
        return getToken(registerSosmedDomain,
                GetTokenUseCase.getParamRegisterWebview(
                        requestParams.getString(GetTokenUseCase.CODE, ""),
                        requestParams.getString(GetTokenUseCase.REDIRECT_URI, "")
                ))
                .flatMap((Func1<LoginEmailDomain, Observable<LoginEmailDomain>>) this::getInfo)
                .flatMap((Func1<LoginEmailDomain, Observable<LoginEmailDomain>>) registerSosmedDomain12 -> {
                    if (registerSosmedDomain12.getInfo().isCreatedPassword()) {
                        return makeLogin(registerSosmedDomain12);
                    } else {
                        return Observable.just(registerSosmedDomain12);
                    }
                });
    }

    public static RequestParams getParamWebview(String code, String redirectUri) {
        RequestParams params = RequestParams.create();
        params.putAll(GetTokenUseCase.getParamRegisterWebview(code, redirectUri).getParameters());
        return params;
    }
}
