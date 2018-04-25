package com.tokopedia.mitratoppers.preapprove.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;
import com.tokopedia.mitratoppers.preapprove.domain.interactor.PreApproveBalanceUseCase;
import com.tokopedia.mitratoppers.preapprove.view.listener.MitraToppersPreApproveView;

import javax.inject.Inject;

import rx.Subscriber;

public class MitraToppersPreApprovePresenter extends BaseDaggerPresenter<MitraToppersPreApproveView> {
    private final PreApproveBalanceUseCase preApproveBalanceUseCase;

    @Inject
    public MitraToppersPreApprovePresenter(PreApproveBalanceUseCase preApproveBalanceUseCase) {
        this.preApproveBalanceUseCase = preApproveBalanceUseCase;
    }

    public void getPreApproveBalanceUseCase() {
        preApproveBalanceUseCase.execute(null, new Subscriber<ResponsePreApprove>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetPreApprove(e);
                }
            }

            @Override
            public void onNext(ResponsePreApprove responsePreApprove) {
                getView().onSuccessGetPreApprove(responsePreApprove);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        preApproveBalanceUseCase.unsubscribe();
    }
}
