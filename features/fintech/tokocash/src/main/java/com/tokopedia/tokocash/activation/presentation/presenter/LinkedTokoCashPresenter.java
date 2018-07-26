package com.tokopedia.tokocash.activation.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.activation.domain.LinkedTokoCashUseCase;
import com.tokopedia.tokocash.activation.presentation.contract.LinkedTokoCashContract;
import com.tokopedia.tokocash.activation.presentation.model.ActivateTokoCashData;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.network.exception.TokoCashException;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class LinkedTokoCashPresenter extends BaseDaggerPresenter<LinkedTokoCashContract.View>
        implements LinkedTokoCashContract.Presenter {

    private LinkedTokoCashUseCase linkedTokoCashUseCase;
    private WalletUserSession walletUserSession;

    @Inject
    public LinkedTokoCashPresenter(LinkedTokoCashUseCase linkedTokoCashUseCase,
                                   WalletUserSession walletUserSession) {
        this.linkedTokoCashUseCase = linkedTokoCashUseCase;
        this.walletUserSession = walletUserSession;
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
                if (e instanceof TokoCashException)
                    getView().onErrorLinkWalletToTokoCash(e);
                else
                    getView().onErrorNetwork(e);
            }

            @Override
            public void onNext(ActivateTokoCashData activateTokoCashData) {
                getView().onSuccessLinkWalletToTokoCash();
            }
        };
    }

    @Override
    public String getUserPhoneNumber() {
        return walletUserSession.getPhoneNumber();
    }

    @Override
    public boolean isMsisdnUserVerified() {
        return walletUserSession.isMsisdnVerified();
    }

    @Override
    public void destroyView() {
        if (linkedTokoCashUseCase != null) linkedTokoCashUseCase.unsubscribe();
    }
}
