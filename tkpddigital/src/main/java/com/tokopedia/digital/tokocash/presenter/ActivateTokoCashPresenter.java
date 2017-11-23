package com.tokopedia.digital.tokocash.presenter;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.digital.tokocash.errorhandle.ResponseTokoCashRuntimeException;
import com.tokopedia.digital.tokocash.interactor.ActivateTokoCashInteractor;
import com.tokopedia.digital.tokocash.listener.ActivateTokoCashView;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashPresenter implements IActivateTokoCashPresenter {

    private final ActivateTokoCashInteractor activateTokoCashInteractor;

    private final ActivateTokoCashView view;

    public ActivateTokoCashPresenter(ActivateTokoCashInteractor activateTokoCashInteractor,
                                     ActivateTokoCashView view) {
        this.activateTokoCashInteractor = activateTokoCashInteractor;
        this.view = view;
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
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    view.onErrorLinkWalletToTokoCash(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException) {
                    view.onErrorLinkWalletToTokoCash(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof ResponseTokoCashRuntimeException) {
                    view.onErrorLinkWalletToTokoCash(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    view.onErrorLinkWalletToTokoCash(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    view.onErrorLinkWalletToTokoCash(e.getMessage());
                } else if (e instanceof ServerErrorException) {
                    ServerErrorHandlerUtil.handleError(e);
                } else {
                    view.onErrorLinkWalletToTokoCash(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(ActivateTokoCashData activateTokoCashData) {
                view.onSuccessLinkWalletToTokoCash();
            }
        };
    }
}
