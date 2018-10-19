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

import javax.inject.Inject;

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
    }

    @Override
    public void onDealRemoved(DealProductViewModel viewModel) {
        int indexOf = getView().getSelectedDeals().indexOf(viewModel);
        if (indexOf != -1) {
            getView().getSelectedDeals().remove(indexOf);
            getView().notifySelectedDealListView(viewModel);
        }

        getView().updateDealListView(viewModel);
    }
}
