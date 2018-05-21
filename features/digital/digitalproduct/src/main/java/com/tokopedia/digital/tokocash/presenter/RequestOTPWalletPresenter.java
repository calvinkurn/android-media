package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.digital.tokocash.errorhandle.ResponseTokoCashRuntimeException;
import com.tokopedia.digital.tokocash.interactor.ActivateTokoCashInteractor;
import com.tokopedia.digital.tokocash.listener.RequestOTPWalletView;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/25/17.
 */

public class RequestOTPWalletPresenter implements IRequestOTPWalletPresenter {

    private final ActivateTokoCashInteractor activateTokoCashInteractor;

    private final RequestOTPWalletView view;

    public RequestOTPWalletPresenter(ActivateTokoCashInteractor activateTokoCashInteractor,
                                     RequestOTPWalletView view) {
        this.activateTokoCashInteractor = activateTokoCashInteractor;
        this.view = view;
    }

    @Override
    public void requestOTPWallet() {
        view.showProgressDialog();
        activateTokoCashInteractor.requestOTPWallet(getSubscriberRequestOTPWallet());
    }

    @Override
    public void linkWalletToTokoCash(String otp) {
        view.showProgressDialog();
        activateTokoCashInteractor.activateTokoCash(otp, getSubscriberLinkWalletToTokoCash());
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
                view.onSuccessLinkWalletToTokoCash();
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
                view.onSuccessRequestOtpWallet();
            }
        };
    }

    private void errorOnRequestOTPWallet(Throwable e) {
        e.printStackTrace();
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            view.onErrorOTPWallet(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            view.onErrorOTPWallet(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseTokoCashRuntimeException) {
            view.onErrorOTPWallet(e.getMessage());
        } else if (e instanceof ResponseDataNullException) {
            view.onErrorOTPWallet(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            view.onErrorOTPWallet(e.getMessage());
        } else if (e instanceof ServerErrorException) {
            ServerErrorHandlerUtil.handleError(e);
        } else {
            view.onErrorOTPWallet(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}
