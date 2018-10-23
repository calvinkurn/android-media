package com.tokopedia.digital.newcart.presentation.presenter;

import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase;
import com.tokopedia.digital.cart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.cart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.util.DigitalAnalytics;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalDealCheckoutContract;
import com.tokopedia.user.session.UserSession;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DigitalDealCheckoutPresenter extends DigitalBaseCartPresenter<DigitalDealCheckoutContract.View>
        implements DigitalDealCheckoutContract.Presenter {

    @Inject
    public DigitalDealCheckoutPresenter(DigitalAddToCartUseCase digitalAddToCartUseCase, DigitalAnalytics digitalAnalytics, DigitalModuleRouter digitalModuleRouter, ICartDigitalInteractor cartDigitalInteractor, UserSession userSession, DigitalCheckoutUseCase digitalCheckoutUseCase, DigitalInstantCheckoutUseCase digitalInstantCheckoutUseCase) {
        super(digitalAddToCartUseCase, digitalAnalytics, digitalModuleRouter, cartDigitalInteractor, userSession, digitalCheckoutUseCase, digitalInstantCheckoutUseCase);
    }

    @Override
    public void onDealsCheckout() {
        renderBaseCart(getView().getCartInfoData());
        getView().renderCategory(getView().getCartInfoData().getAttributes().getCategoryName());
        collapseCartDetailAfter5Seconds();
    }

    private void collapseCartDetailAfter5Seconds() {
        Observable.timer(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (isViewAttached()) {
                            if (getView().isCartDetailViewVisible()) {
                                getView().hideCartDetailView();
                                getView().hideDealsContainerView();
                                getView().renderIconToExpand();
                            }
                        }
                    }
                });
    }

    @Override
    public void onExpandCollapseButtonView() {
        if (getView().isCartDetailViewVisible()) {
            getView().hideCartDetailView();
            getView().hideDealsContainerView();
            getView().renderIconToExpand();
        } else {
            getView().showCartDetailView();
            getView().showDealsContainerView();
            getView().renderIconToCollapse();
        }
    }

    @Override
    public void onNewSelectedDeal(DealProductViewModel viewModel) {
        getView().addSelectedDeal(viewModel);
        getView().renderCartDealListView(viewModel);
        getView().renderCategory(
                String.format("%s & %d Deals", getView().getCartInfoData().getAttributes().getCategoryName(), getView().getSelectedDeals().size())
        );
    }

    @Override
    public void onDealRemoved(DealProductViewModel viewModel) {
        int indexOf = getView().getSelectedDeals().indexOf(viewModel);
        if (indexOf != -1) {
            getView().getSelectedDeals().remove(indexOf);
            getView().notifySelectedDealListView(viewModel);
        }

        getView().updateDealListView(viewModel);
        if (getView().getSelectedDeals().size() == 0) {
            getView().hideDealsContainerView();
        }
    }
}
