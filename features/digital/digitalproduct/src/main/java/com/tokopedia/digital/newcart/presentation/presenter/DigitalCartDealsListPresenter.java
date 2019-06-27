package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.newcart.domain.DigitalDealGetProductsUseCase;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductsViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsListContract;

import javax.inject.Inject;

import rx.Subscriber;

public class DigitalCartDealsListPresenter extends BaseDaggerPresenter<DigitalCartDealsListContract.View> implements DigitalCartDealsListContract.Presenter {

    private DigitalDealGetProductsUseCase digitalDealGetProductsUseCase;
    private DigitalAnalytics digitalAnalytics;

    @Inject
    public DigitalCartDealsListPresenter(DigitalDealGetProductsUseCase digitalDealGetProductsUseCase,
                                         DigitalAnalytics digitalAnalytics) {
        this.digitalDealGetProductsUseCase = digitalDealGetProductsUseCase;
        this.digitalAnalytics = digitalAnalytics;
    }

    @Override
    public void getProducts(String nextUrl, String categoryName) {
        digitalDealGetProductsUseCase.execute(
                digitalDealGetProductsUseCase.createRequest(nextUrl, categoryName),
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
                        if (dealProductsViewModel.getNextUrl() == null || dealProductsViewModel.getNextUrl().length() == 0) {
                            getView().renderList(dealProductsViewModel.getProducts());
                        } else {
                            getView().setNextUrl(dealProductsViewModel.getNextUrl());
                            getView().renderList(dealProductsViewModel.getProducts(), true);
                        }
                    }
                });
    }

    @Override
    public void onDealDetailClicked(DealProductViewModel productViewModel) {
        getView().navigateToDetailPage(productViewModel);
    }

    @Override
    public void onBuyButtonClicked(DealProductViewModel productViewModel) {
        digitalAnalytics.eventAddDeal(productViewModel);
    }

    @Override
    public void detachView() {
        digitalDealGetProductsUseCase.unsubscribe();
        super.detachView();
    }
}
