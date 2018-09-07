package com.tokopedia.tokocash.activation.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.activation.domain.LinkedTokoCashUseCase;
import com.tokopedia.tokocash.activation.domain.RequestOtpTokoCashUseCase;
import com.tokopedia.tokocash.activation.presentation.contract.RequestOtpTokoCashContract;
import com.tokopedia.tokocash.activation.presentation.model.ActivateTokoCashData;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.network.exception.TokoCashException;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/25/17.
 */

public class RequestOTPWalletPresenter extends BaseDaggerPresenter<RequestOtpTokoCashContract.View>
        implements RequestOtpTokoCashContract.Presenter {

    private RequestOtpTokoCashUseCase requestOtpTokoCashUseCase;
    private LinkedTokoCashUseCase linkedTokoCashUseCase;
    private WalletUserSession walletUserSession;

    @Inject
    public RequestOTPWalletPresenter(RequestOtpTokoCashUseCase requestOtpTokoCashUseCase,
                                     LinkedTokoCashUseCase linkedTokoCashUseCase,
                                     WalletUserSession walletUserSession) {
        this.requestOtpTokoCashUseCase = requestOtpTokoCashUseCase;
        this.linkedTokoCashUseCase = linkedTokoCashUseCase;
        this.walletUserSession = walletUserSession;
    }

    @Override
    public void requestOTPWallet() {
        getView().showProgressDialog();
        requestOtpTokoCashUseCase.execute(RequestParams.EMPTY, getSubscriberRequestOTPWallet());
    }

    @Override
    public void linkWalletToTokoCash(String otp) {
        getView().showProgressDialog();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(LinkedTokoCashUseCase.OTP_CODE, otp);
        linkedTokoCashUseCase.execute(requestParams, getSubscriberLinkWalletToTokoCash());
    }

    private Subscriber<ActivateTokoCashData> getSubscriberLinkWalletToTokoCash() {
        return new Subscriber<ActivateTokoCashData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                errorOnRequestOTPWallet(e);
            }

            @Override
            public void onNext(ActivateTokoCashData activateTokoCashData) {
                getView().onSuccessLinkWalletToTokoCash();
            }
        };
    }

    private Subscriber<ActivateTokoCashData> getSubscriberRequestOTPWallet() {
        return new Subscriber<ActivateTokoCashData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                errorOnRequestOTPWallet(e);
            }

            @Override
            public void onNext(ActivateTokoCashData activateTokoCashData) {
                getView().onSuccessRequestOtpWallet();
            }
        };
    }

    private void errorOnRequestOTPWallet(Throwable e) {
        e.printStackTrace();
        if (e instanceof TokoCashException)
            getView().onErrorOTPWallet(e);
        else
            getView().onErrorNetwork(e);

    }

    @Override
    public String getUserPhoneNumber() {
        return walletUserSession.getPhoneNumber();
    }

    @Override
    public void setMsisdnUserVerified(boolean verified) {
        walletUserSession.setMsisdnVerified(verified);
    }

    @Override
    public void onDestroyView() {
        if (requestOtpTokoCashUseCase != null) requestOtpTokoCashUseCase.unsubscribe();
        if (linkedTokoCashUseCase != null) linkedTokoCashUseCase.unsubscribe();
    }
}
