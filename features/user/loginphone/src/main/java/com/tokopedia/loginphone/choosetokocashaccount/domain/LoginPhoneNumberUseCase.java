package com.tokopedia.loginphone.choosetokocashaccount.domain;

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

public class LoginPhoneNumberUseCase extends UseCase<LoginEmailDomain> {

    private GetTokenUseCase getTokenUseCase;
    private MakeLoginUseCase makeLoginUseCase;
    private GetUserInfoUseCase getUserInfoUseCase;

    @Inject
    public LoginPhoneNumberUseCase(GetTokenUseCase getTokenUseCase,
                                   GetUserInfoUseCase getUserInfoUseCase,
                                   MakeLoginUseCase makeLoginUseCase) {
        this.getTokenUseCase = getTokenUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public Observable<LoginEmailDomain> createObservable(RequestParams requestParams) {
        final LoginEmailDomain loginTokoCashViewModel = new LoginEmailDomain();
        return Observable.just(loginTokoCashViewModel)
                .flatMap(getTokenAccounts(requestParams))
                .flatMap(getUserInfo())
                .flatMap(makeLogin(requestParams));
    }

    private Func1<LoginEmailDomain, Observable<LoginEmailDomain>> getUserInfo() {
        return loginTokoCashViewModel -> getUserInfoUseCase.createObservable(GetUserInfoUseCase.generateParam())
                .flatMap((Func1<GetUserInfoData, Observable<LoginEmailDomain>>) getUserInfoDomainModel -> {
                    loginTokoCashViewModel.setInfo(getUserInfoDomainModel);
                    return Observable.just(loginTokoCashViewModel);
                });
    }

    private Func1<LoginEmailDomain, Observable<LoginEmailDomain>> makeLogin(RequestParams requestParams) {
        return loginTokoCashViewModel -> makeLoginUseCase.createObservable(getMakeLoginParam
                (loginTokoCashViewModel, requestParams))
                .flatMap((Func1<MakeLoginPojo, Observable<LoginEmailDomain>>) loginResult -> {
                    loginTokoCashViewModel.setLoginResult(loginResult);
                    return Observable.just(loginTokoCashViewModel);
                });
    }

    private RequestParams getMakeLoginParam(LoginEmailDomain loginTokoCashViewModel, RequestParams requestParams) {
        return MakeLoginUseCase.getParam(String.valueOf(loginTokoCashViewModel.getInfo().getUserId()),
                requestParams.getString(MakeLoginUseCase.PARAM_DEVICE_ID, "")
        );
    }

    private Func1<LoginEmailDomain, Observable<LoginEmailDomain>> getTokenAccounts(RequestParams requestParams) {
        return loginTokoCashViewModel -> getTokenUseCase.createObservable(getTokenParam(requestParams))
                .flatMap((Func1<TokenViewModel, Observable<LoginEmailDomain>>) tokenViewModel -> {
                    loginTokoCashViewModel.setToken(tokenViewModel);
                    return Observable.just(loginTokoCashViewModel);
                });
    }

    private RequestParams getTokenParam(RequestParams requestParams) {
        return GetTokenUseCase.getParamLoginRegisterPhoneNumber(
                requestParams.getString(GetTokenUseCase.PASSWORD, ""),
                requestParams.getString(GetTokenUseCase.USER_NAME, ""),
                requestParams.getString(GetTokenUseCase.CODE, ""));
    }

    public static RequestParams getParam(String accessToken,
                                         String email,
                                         String userId,
                                         String deviceId,
                                         String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putAll(GetTokenUseCase.getParamLoginRegisterPhoneNumber(
                accessToken, email, phoneNumber).getParameters());
        params.putAll(MakeLoginUseCase.getParam(userId, deviceId).getParameters());
        return params;
    }


}
