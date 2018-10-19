package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.newcart.domain.DigitalDealGetProductsUseCase;
import com.tokopedia.digital.newcart.domain.model.DealProductsViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsListContract;

import javax.inject.Inject;

import rx.Subscriber;

public class DigitalCartDealsListPresenter extends BaseDaggerPresenter<DigitalCartDealsListContract.View> implements DigitalCartDealsListContract.Presenter {

    private DigitalDealGetProductsUseCase digitalDealGetProductsUseCase;

    @Inject
    public DigitalCartDealsListPresenter(DigitalDealGetProductsUseCase digitalDealGetProductsUseCase) {
        this.digitalDealGetProductsUseCase = digitalDealGetProductsUseCase;
    }

    @Override
    public void getProducts(String nextUrl) {
        digitalDealGetProductsUseCase.execute(
                digitalDealGetProductsUseCase.createRequest(nextUrl),
                new Subscriber<DealProductsViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().showGetListError(e);
                        }
                    }

                    @Override
                    public void onNext(DealProductsViewModel dealProductsViewModel) {
                        getView().showDealTagline();
                        if (dealProductsViewModel.getNextUrl() == null || dealProductsViewModel.getNextUrl().length() == 0) {
                            getView().renderList(dealProductsViewModel.getProducts());
                        } else {
                            getView().setNextUrl(dealProductsViewModel.getNextUrl());
                            getView().renderList(dealProductsViewModel.getProducts(), true);
                        }
                    }
                });
    }
}
