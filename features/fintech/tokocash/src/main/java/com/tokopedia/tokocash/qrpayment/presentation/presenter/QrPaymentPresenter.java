package com.tokopedia.tokocash.qrpayment.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.PostQrPaymentUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.contract.QrPaymentContract;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class QrPaymentPresenter extends BaseDaggerPresenter<QrPaymentContract.View>
        implements QrPaymentContract.Presenter {

    private PostQrPaymentUseCase postQrPaymentUseCase;
    private GetBalanceTokoCashUseCase getBalanceTokoCashUseCase;

    @Inject
    public QrPaymentPresenter(PostQrPaymentUseCase postQrPaymentUseCase,
                              GetBalanceTokoCashUseCase getBalanceTokoCashUseCase) {
        this.postQrPaymentUseCase = postQrPaymentUseCase;
        this.getBalanceTokoCashUseCase = getBalanceTokoCashUseCase;
    }

    @Override
    public void postQrPayment() {
        postQrPaymentUseCase.execute(getView().getRequestParams(), new Subscriber<QrPaymentTokoCash>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().directToFailedPayment();
            }

            @Override
            public void onNext(QrPaymentTokoCash qrPaymentTokoCash) {
                getView().directToSuccessPayment(qrPaymentTokoCash);
            }
        });
    }

    @Override
    public void getBalanceTokoCash() {
        getBalanceTokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<BalanceTokoCash>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showErrorBalanceTokoCash(e);
            }

            @Override
            public void onNext(BalanceTokoCash balanceTokoCash) {
                if (balanceTokoCash != null) {
                    getView().renderBalanceTokoCash(balanceTokoCash);
                }
            }
        });
    }

    @Override
    public void onDestroyPresenter() {
        if (getBalanceTokoCashUseCase != null) getBalanceTokoCashUseCase.unsubscribe();
        if (postQrPaymentUseCase != null) postQrPaymentUseCase.unsubscribe();
    }
}