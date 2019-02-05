package com.tokopedia.logisticinsurance.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.logisticinsurance.domain.InsuranceTnCUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCPresenter extends BaseDaggerPresenter<InsuranceTnCContract.View>
        implements InsuranceTnCContract.Presenter {

    private InsuranceTnCUseCase insuranceTnCUseCase;

    @Inject
    InsuranceTnCContract.Presenter presenter;

    @Inject
    public InsuranceTnCPresenter(InsuranceTnCUseCase insuranceTnCUseCase) {
        this.insuranceTnCUseCase = insuranceTnCUseCase;
    }

    @Override
    public void attachView(InsuranceTnCContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        insuranceTnCUseCase.unsubscribe();
    }

    @Override
    public void loadWebViewData() {
        getView().showLoading();
        insuranceTnCUseCase.execute(RequestParams.create(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoading();
                }
            }

            @Override
            public void onNext(String webViewData) {
                if (isViewAttached()) {
                    getView().showWebView(webViewData);
                }
            }
        });
    }

}
