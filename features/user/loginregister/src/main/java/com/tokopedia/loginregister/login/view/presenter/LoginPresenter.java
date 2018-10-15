package com.tokopedia.loginregister.login.view.presenter;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.loginregister.common.util.ErrorHandlerSession;
import com.tokopedia.loginregister.login.di.LoginModule;
import com.tokopedia.loginregister.login.domain.DiscoverUseCase;
import com.tokopedia.loginregister.login.view.listener.LoginContract;
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

/**
 * @author by nisie on 10/2/18.
 */
public class LoginPresenter extends BaseDaggerPresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private static final String LOGIN_CACHE_KEY = "LOGIN_ID";
    private static final String BUNDLE = "bundle";
    private static final String ERROR = "error";
    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private static final String SERVER = "server";
    private static final String PATH = "path";
    private static final String HTTPS = "https://";
    private static final String ACTIVATION_SOCIAL = "activation-social";

    private final LocalCacheHandler loginCache;
    private final DiscoverUseCase discoverUseCase;
//    private final GetFacebookCredentialUseCase getFacebookCredentialUseCase;
//    private final LoginWithSosmedUseCase loginWithSosmedUseCase;
//    private final LoginWebviewUseCase loginWebviewUseCase;
//    private final LoginEmailUseCase loginEmailUseCase;

    @Inject
    public LoginPresenter(
            @Named(LoginModule.LOGIN_CACHE) LocalCacheHandler loginCache,
//                          SessionHandler sessionHandler,
//                          LoginEmailUseCase loginEmailUseCase,
            DiscoverUseCase discoverUseCase
//                          GetFacebookCredentialUseCase getFacebookCredentialUseCase,
//                          LoginWithSosmedUseCase loginWithSosmedUseCase,
//                          LoginWebviewUseCase loginWebviewUseCase
    ) {
        this.loginCache = loginCache;
//        this.loginEmailUseCase = loginEmailUseCase;
        this.discoverUseCase = discoverUseCase;
//        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
//        this.loginWithSosmedUseCase = loginWithSosmedUseCase;
//        this.loginWebviewUseCase = loginWebviewUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        discoverUseCase.unsubscribe();
//        loginEmailUseCase.unsubscribe();
//        loginWithSosmedUseCase.unsubscribe();
//        loginWebviewUseCase.unsubscribe();
    }

    @Override
    public void login(String email, String password) {

    }

    @Override
    public void saveLoginEmail(String email) {

    }

    @Override
    public ArrayList<String> getLoginIdList() {
        return loginCache.getArrayListString(LOGIN_CACHE_KEY);
    }

    @Override
    public void discoverLogin() {
        getView().showLoadingDiscover();
        discoverUseCase.execute(RequestParams.EMPTY, new Subscriber<DiscoverViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().dismissLoadingDiscover();
                ErrorHandlerSession.getErrorMessage(new ErrorHandlerSession.ErrorForbiddenListener
                        () {
                    @Override
                    public void onForbidden() {
                        getView().onForbidden();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        getView().onErrorDiscoverLogin(errorMessage);
                    }
                }, e, getView().getContext());
            }

            @Override
            public void onNext(DiscoverViewModel discoverViewModel) {
                getView().dismissLoadingDiscover();
                if (!discoverViewModel.getProviders().isEmpty()) {
                    getView().onSuccessDiscoverLogin(discoverViewModel.getProviders());
                } else {
                    getView().onErrorDiscoverLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(
                            ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                            getView().getContext()));
                }
            }
        });
    }

    @Override
    public void loginWebview(Intent data) {

    }

    @Override
    public void loginGoogle(String accessToken, String email) {

    }

    @Override
    public void getFacebookCredential(Fragment fragment, CallbackManager callbackManager) {

    }

    @Override
    public void loginFacebook(AccessToken accessToken, String email) {

    }
}
