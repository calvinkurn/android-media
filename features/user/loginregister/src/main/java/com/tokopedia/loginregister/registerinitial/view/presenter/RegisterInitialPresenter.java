package com.tokopedia.loginregister.registerinitial.view.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase;
import com.tokopedia.loginregister.login.view.model.DiscoverViewModel;
import com.tokopedia.loginregister.loginthirdparty.domain.LoginWebviewUseCase;
import com.tokopedia.loginregister.loginthirdparty.domain.LoginWithSosmedUseCase;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase;
import com.tokopedia.loginregister.loginthirdparty.webview.WebViewLoginFragment;
import com.tokopedia.loginregister.registerinitial.domain.usecase.RegisterValidationUseCase;
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo;
import com.tokopedia.loginregister.registerinitial.view.listener.RegisterInitialContract;
import com.tokopedia.loginregister.registerinitial.view.subscriber.RegisterThirdPartySubscriber;
import com.tokopedia.loginregister.ticker.domain.usecase.TickerInfoUseCase;
import com.tokopedia.loginregister.ticker.subscriber.TickerInfoRegisterSubscriber;
import com.tokopedia.sessioncommon.ErrorHandlerSession;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 10/24/18.
 */
public class RegisterInitialPresenter extends BaseDaggerPresenter<RegisterInitialContract.View>
        implements RegisterInitialContract.Presenter {

    private static final String BUNDLE_WEBVIEW = "bundle";
    private static final String ARGS_PATH = "path";
    private static final String ARGS_ERROR = "error";
    private static final String ARGS_MESSAGE = "message";
    private static final String ARGS_CODE = "code";
    private static final String ARGS_SERVER = "server";
    private static final String HTTPS = "https://";
    private static final String PHONE_TYPE = "phone";
    private static final String EMAIL_TYPE = "email";

    private final DiscoverUseCase discoverUseCase;
    private final GetFacebookCredentialUseCase getFacebookCredentialUseCase;
    private final LoginWithSosmedUseCase loginSosmedUseCase;
    private final LoginWebviewUseCase registerWebviewUseCase;
    private final RegisterValidationUseCase registerValidationUseCase;
    private final TickerInfoUseCase tickerInfoUseCase;

    @Inject
    public RegisterInitialPresenter(
            DiscoverUseCase discoverUseCase,
            GetFacebookCredentialUseCase getFacebookCredentialUseCase,
            LoginWithSosmedUseCase loginSosmedUseCase,
            LoginWebviewUseCase registerWebviewUseCase,
            RegisterValidationUseCase registerValidationUseCase,
            TickerInfoUseCase tickerInfoUseCase) {
        this.discoverUseCase = discoverUseCase;
        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
        this.loginSosmedUseCase = loginSosmedUseCase;
        this.registerWebviewUseCase = registerWebviewUseCase;
        this.registerValidationUseCase = registerValidationUseCase;
        this.tickerInfoUseCase = tickerInfoUseCase;
    }

    @Override
    public void getProvider() {
        getView().showLoadingDiscover();
        discoverUseCase.execute(DiscoverUseCase.getParamRegister(), new Subscriber<DiscoverViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().dismissLoadingDiscover();
                ErrorHandlerSession.getErrorMessage(new ErrorHandlerSession.ErrorForbiddenListener() {
                    @Override
                    public void onForbidden() {
                        getView().getLoginRouter().onForbidden();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        getView().onErrorDiscoverRegister(errorMessage);
                    }
                }, e, getView().getContext());
            }

            @Override
            public void onNext(DiscoverViewModel discoverViewModel) {
                getView().dismissLoadingDiscover();
                if (!discoverViewModel.getProviders().isEmpty()) {
                    getView().onSuccessDiscoverRegister(discoverViewModel.getProviders());
                } else {
                    getView().onErrorDiscoverRegister(
                            getView().getContext().getString(R.string.error_empty_provider)
                                    + " "
                                    + getView().getContext().getString(R.string.code_error)
                                    + " " + ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW);
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        discoverUseCase.unsubscribe();
        loginSosmedUseCase.unsubscribe();
        registerWebviewUseCase.unsubscribe();
        registerValidationUseCase.unsubscribe();
    }

    @Override
    public void validateRegister(String id) {
        registerValidationUseCase.execute(RegisterValidationUseCase.createValidateRegisterParam(id),
                new Subscriber<RegisterValidationPojo>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onErrorValidateRegister(
                                ErrorHandlerSession.getErrorMessage(getView().getContext(),
                                        throwable));

                    }

                    @Override
                    public void onNext(RegisterValidationPojo registerValidationViewModel) {
                        onSuccessValidate(registerValidationViewModel);
                    }
                });
    }

    private void onSuccessValidate(RegisterValidationPojo model) {
        if (TextUtils.equals(model.getType(), PHONE_TYPE)) {
            getView().setTempPhoneNumber(model.getView());
            if (model.getExist()) {
                getView().showRegisteredPhoneDialog(model.getView());
            } else {
                getView().showProceedWithPhoneDialog(model.getView());
            }
        }

        if (TextUtils.equals(model.getType(), EMAIL_TYPE)) {
            if (model.getExist()) {
                getView().showRegisteredEmailDialog(model.getView());
            } else {
                getView().goToRegisterEmailPageWithEmail(model.getView());
            }
        }
    }

    @Override
    public void registerWebview(Intent data) {
        getView().showProgressBar();
        Bundle bundle = data.getBundleExtra(BUNDLE_WEBVIEW);
        String methodName = bundle != null ? bundle.getString(WebViewLoginFragment.NAME,
                LoginRegisterAnalytics.WEBVIEW) : LoginRegisterAnalytics.WEBVIEW;

        if (bundle != null && bundle.getString(ARGS_PATH, "").contains(ARGS_ERROR)) {
            getView().dismissProgressBar();
            getView().onErrorRegisterSosmed(methodName,
                    bundle.getString(ARGS_MESSAGE)
                            + " " + ErrorHandlerSession.ErrorCode.EMPTY_ACCESS_TOKEN);
        } else if (bundle != null
                && bundle.getString(ARGS_PATH) != null
                && bundle.getString(ARGS_CODE) != null
                && bundle.getString(ARGS_SERVER) != null) {
            registerWebviewUseCase.execute(LoginWebviewUseCase.getParamWebview(
                    bundle.getString(ARGS_CODE),
                    HTTPS + bundle.getString(ARGS_SERVER) + bundle.getString(ARGS_PATH)
            ), new RegisterThirdPartySubscriber(getView().getContext(), getView()
                    .getLoginRouter(), "", getView(), methodName));
        } else {
            getView().dismissProgressBar();
            getView().onErrorRegisterSosmed(
                    methodName, ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode
                            .EMPTY_ACCESS_TOKEN, getView().getContext()));
        }
    }

    @Override
    public void getFacebookCredential(Fragment fragment, CallbackManager
            callbackManager) {
        getFacebookCredentialUseCase.execute(
                GetFacebookCredentialUseCase.getParam(
                        fragment,
                        callbackManager),
                new GetFacebookCredentialSubscriber(getView().getFacebookCredentialListener()));
    }

    @Override
    public void registerFacebook(AccessToken accessToken, String email) {
        getView().showProgressBar();
        loginSosmedUseCase.execute(
                LoginWithSosmedUseCase.getParamFacebook(accessToken),
                new RegisterThirdPartySubscriber(getView().getContext(),
                        getView().getLoginRouter(), email, getView(),
                        LoginRegisterAnalytics.FACEBOOK)
        );
    }

    @Override
    public void registerGoogle(String accessToken, String email) {
        getView().showProgressBar();
        loginSosmedUseCase.execute(
                LoginWithSosmedUseCase.getParamGoogle(accessToken),
                new RegisterThirdPartySubscriber(getView().getContext(),
                        getView().getLoginRouter(), email, getView(),
                        LoginRegisterAnalytics.GOOGLE)
        );
    }

    @Override
    public void getTickerInfo(){
        tickerInfoUseCase.execute(TickerInfoUseCase.createRequestParam(TickerInfoUseCase.REGISTER_PAGE),
                new TickerInfoRegisterSubscriber(getView()));
    }
}
