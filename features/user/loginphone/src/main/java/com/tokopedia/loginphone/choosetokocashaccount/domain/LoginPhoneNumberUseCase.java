package com.tokopedia.loginphone.choosetokocashaccount.domain;

import com.tokopedia.loginphone.choosetokocashaccount.data.GetCodeTokoCashPojo;
import com.tokopedia.loginphone.choosetokocashaccount.data.LoginTokoCashViewModel;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;
import com.tokopedia.sessioncommon.data.model.TokenViewModel;
import com.tokopedia.sessioncommon.domain.usecase.GetTokenUseCase;
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoUseCase;
import com.tokopedia.sessioncommon.domain.usecase.MakeLoginUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 12/5/17.
 */

public class LoginPhoneNumberUseCase extends UseCase<LoginTokoCashViewModel> {

    private GetCodeTokoCashUseCase getCodeTokoCashUseCase;
    private GetTokenUseCase getTokenUseCase;
    private MakeLoginUseCase makeLoginUseCase;
    private GetUserInfoUseCase getUserInfoUseCase;

    @Inject
    public LoginPhoneNumberUseCase(GetCodeTokoCashUseCase getCodeTokoCashUseCase,
                                   GetTokenUseCase getTokenUseCase,
                                   GetUserInfoUseCase getUserInfoUseCase,
                                   MakeLoginUseCase makeLoginUseCase) {
        this.getCodeTokoCashUseCase = getCodeTokoCashUseCase;
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public Observable<LoginTokoCashViewModel> createObservable(RequestParams requestParams) {
        final LoginTokoCashViewModel loginTokoCashViewModel = new LoginTokoCashViewModel();
        return Observable.just(loginTokoCashViewModel)
                .flatMap(getCodeTokoCash(requestParams))
                .flatMap(getTokenAccounts())
                .flatMap(getUserInfo())
                .flatMap(makeLogin(requestParams));
    }

    private Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>> getUserInfo() {
        return loginTokoCashViewModel -> getUserInfoUseCase.createObservable(GetUserInfoUseCase.generateParam())
                .flatMap((Func1<GetUserInfoData, Observable<LoginTokoCashViewModel>>) getUserInfoDomainModel -> {
                    loginTokoCashViewModel.setInfo(getUserInfoDomainModel);
                    return Observable.just(loginTokoCashViewModel);
                });
    }

    private Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>> makeLogin(RequestParams requestParams) {
        return loginTokoCashViewModel -> makeLoginUseCase.createObservable(getMakeLoginParam
                (loginTokoCashViewModel, requestParams))
                .flatMap((Func1<MakeLoginPojo, Observable<LoginTokoCashViewModel>>) loginResult -> {
                    loginTokoCashViewModel.setLoginResult(loginResult);
                    return Observable.just(loginTokoCashViewModel);
                });
    }

    private RequestParams getMakeLoginParam(LoginTokoCashViewModel loginTokoCashViewModel, RequestParams requestParams) {
        return MakeLoginUseCase.getParam(String.valueOf(loginTokoCashViewModel.getInfo().getUserId()),
                requestParams.getString(MakeLoginUseCase.PARAM_DEVICE_ID, "")
        );
    }

    private Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>> getTokenAccounts() {
        return loginTokoCashViewModel -> getTokenUseCase.createObservable(getTokenParam(loginTokoCashViewModel))
                .flatMap((Func1<TokenViewModel, Observable<LoginTokoCashViewModel>>) tokenViewModel -> {
                    loginTokoCashViewModel.setToken(tokenViewModel);
                    return Observable.just(loginTokoCashViewModel);
                });
    }

    private RequestParams getTokenParam(LoginTokoCashViewModel loginTokoCashViewModel) {
        return GetTokenUseCase.getParamThirdParty(GetTokenUseCase.SOCIAL_TYPE_PHONE_NUMBER,
                loginTokoCashViewModel.getTokoCashCode().getCode());
    }

    private Func1<LoginTokoCashViewModel, Observable<LoginTokoCashViewModel>> getCodeTokoCash
            (final RequestParams requestParams) {
        return loginTokoCashViewModel -> getCodeTokoCashUseCase.createObservable(getAccessTokenParams(requestParams))
                .flatMap((Func1<GetCodeTokoCashPojo, Observable<LoginTokoCashViewModel>>) accessTokenTokoCashDomain -> {
                    loginTokoCashViewModel.setTokoCashCode(accessTokenTokoCashDomain);
                    return Observable.just(loginTokoCashViewModel);
                });
    }

    private RequestParams getAccessTokenParams(RequestParams requestParams) {
        return GetCodeTokoCashUseCase.getParam(
                requestParams.getString(GetCodeTokoCashUseCase.PARAM_KEY, ""),
                requestParams.getString(GetCodeTokoCashUseCase.PARAM_EMAIL, ""));
    }

    public static RequestParams getParam(String accessToken, String email, String userId, String
            deviceId) {
        RequestParams params = RequestParams.create();
        params.putAll(GetCodeTokoCashUseCase.getParam(accessToken, email).getParameters());
        params.putAll(MakeLoginUseCase.getParam(userId, deviceId).getParameters());
        return params;
    }


}
