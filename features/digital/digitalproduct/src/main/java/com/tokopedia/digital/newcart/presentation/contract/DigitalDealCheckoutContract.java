package com.tokopedia.digital.newcart.presentation.contract;

import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;

import java.util.List;

public interface DigitalDealCheckoutContract {
    interface View extends DigitalBaseContract.View{

        boolean isCartDetailViewVisible();

        void hideCartDetailView();

        void showCartDetailView();

        void renderIconToExpand();

        void renderIconToCollapse();

        void addSelectedDeal(DealProductViewModel viewModel);

        List<DealProductViewModel> getSelectedDeals();

        void renderCartDealListView(DealProductViewModel newSelectedDeal);

        void notifySelectedDealListView(DealProductViewModel viewModel);

        void updateDealListView(DealProductViewModel viewModel);

        void hideDealsContainerView();

        void showDealsContainerView();
    }

    interface Presenter extends DigitalBaseContract.Presenter<View>{

        void onDealsCheckout();

        void onExpandCollapseButtonView();

        void onNewSelectedDeal(DealProductViewModel viewModel);

        void onDealRemoved(DealProductViewModel viewModel);
    }
}
