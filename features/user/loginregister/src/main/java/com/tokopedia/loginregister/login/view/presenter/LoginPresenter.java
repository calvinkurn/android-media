package com.tokopedia.loginregister.login.view.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase;
import com.tokopedia.loginregister.login.di.LoginModule;
import com.tokopedia.loginregister.login.view.listener.LoginContract;
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel;
import com.tokopedia.loginregister.login.view.subscriber.LoginSubscriber;
import com.tokopedia.loginregister.loginthirdparty.domain.LoginWebviewUseCase;
import com.tokopedia.loginregister.loginthirdparty.domain.LoginWithSosmedUseCase;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase;
import com.tokopedia.loginregister.loginthirdparty.subscriber.LoginThirdPartySubscriber;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.domain.usecase.LoginEmailUseCase;
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
    private final GetFacebookCredentialUseCase getFacebookCredentialUseCase;
    private final LoginWithSosmedUseCase loginWithSosmedUseCase;
    private final LoginWebviewUseCase loginWebviewUseCase;
    private final LoginEmailUseCase loginEmailUseCase;

    @Inject
    public LoginPresenter(
            @Named(LoginModule.LOGIN_CACHE) LocalCacheHandler loginCache,
            LoginEmailUseCase loginEmailUseCase,
            DiscoverUseCase discoverUseCase,
            GetFacebookCredentialUseCase getFacebookCredentialUseCase,
            LoginWithSosmedUseCase loginWithSosmedUseCase,
            LoginWebviewUseCase loginWebviewUseCase
    ) {
        this.loginCache = loginCache;
        this.loginEmailUseCase = loginEmailUseCase;
        this.discoverUseCase = discoverUseCase;
        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
        this.loginWithSosmedUseCase = loginWithSosmedUseCase;
        this.loginWebviewUseCase = loginWebviewUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        discoverUseCase.unsubscribe();
        loginEmailUseCase.unsubscribe();
        loginWithSosmedUseCase.unsubscribe();
        loginWebviewUseCase.unsubscribe();
    }

    @Override
    public void login(String email, String password) {
        getView().resetError();
        if (isValid(email, password)) {
            getView().showLoadingLogin();
            getView().disableArrow();
            loginEmailUseCase.execute(LoginEmailUseCase.getParam(email, password),
                    new LoginSubscriber(getView().getContext(), getView().getLoginRouter(),
                            email, getView()));
        }
    }

    private boolean isValid(String email, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(password)) {
            getView().showErrorPassword(R.string.error_field_required);
            isValid = false;
        } else if (password.length() < 4) {
            getView().showErrorPassword(R.string.error_incorrect_password);
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            getView().showErrorEmail(R.string.error_field_required);
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            getView().showErrorEmail(R.string.error_invalid_email);
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void saveLoginEmail(String email) {
        ArrayList<String> listId = loginCache.getArrayListString(LOGIN_CACHE_KEY);
        if (!TextUtils.isEmpty(email) && !listId.contains(email)) {
            listId.add(email);
            loginCache.putArrayListString(LOGIN_CACHE_KEY, listId);
            loginCache.applyEditor();
            getView().setAutoCompleteAdapter(listId);
        }
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
                        getView().getLoginRouter().onForbidden();
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
        if (data.getBundleExtra(BUNDLE) != null
                && data.getBundleExtra(BUNDLE).getString(PATH) != null) {
            Bundle bundle = data.getBundleExtra(BUNDLE);
            if (bundle.getString(PATH, "").contains(ERROR)) {
                getView().onErrorLoginSosmed(LoginRegisterAnalytics.WEBVIEW,
                        bundle.getString(MESSAGE, "")
                                + getView().getContext().getString(R.string.code_error) + " " +
                                ErrorHandlerSession.ErrorCode.WS_ERROR);
            } else if (bundle.getString(PATH, "").contains(CODE)) {
                getView().showLoadingLogin();
                loginWebviewUseCase.execute(LoginWebviewUseCase.getParamWebview(bundle.getString
                                (CODE, ""), HTTPS + bundle.getString(SERVER) + bundle.getString
                                (PATH)),
                        new LoginThirdPartySubscriber(getView().getContext(),
                                getView().getLoginRouter(),
                                "", getView(), LoginRegisterAnalytics.WEBVIEW));
            } else if (bundle.getString(PATH, "").contains(ACTIVATION_SOCIAL)) {
                getView().onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(
                        ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                        getView().getContext()));
            }
        } else {
            getView().onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(
                    ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                    getView().getContext()));
        }
    }

    @Override
    public void loginGoogle(String accessToken, String email) {
        getView().showLoadingLogin();
        loginWithSosmedUseCase.execute(LoginWithSosmedUseCase.getParamGoogle(accessToken),
                new LoginThirdPartySubscriber(getView().getContext(), getView().getLoginRouter(),
                        email, getView(), LoginRegisterAnalytics.GOOGLE));
    }

    @Override
    public void getFacebookCredential(Fragment fragment, CallbackManager callbackManager) {
        getFacebookCredentialUseCase.execute(GetFacebookCredentialUseCase.getParam(
                fragment,
                callbackManager),
                new GetFacebookCredentialSubscriber(getView().getFacebookCredentialListener()));
    }

    @Override
    public void loginFacebook(AccessToken accessToken, String email) {
        getView().showLoadingLogin();
        loginWithSosmedUseCase.execute(LoginWithSosmedUseCase.getParamFacebook(accessToken),
                new LoginThirdPartySubscriber(getView().getContext(), getView().getLoginRouter(),
                        email, getView(), LoginRegisterAnalytics.FACEBOOK));
    }
}
