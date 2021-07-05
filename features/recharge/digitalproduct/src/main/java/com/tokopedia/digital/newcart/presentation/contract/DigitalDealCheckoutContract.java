package com.tokopedia.digital.newcart.presentation.contract;

import androidx.annotation.DimenRes;
import androidx.annotation.StringRes;

import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.model.checkout.CheckoutDataParameter;

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

        void updateToolbarTitle(@StringRes int resId);

        void setCheckoutParameter(CheckoutDataParameter.Builder builder);

        long getCheckoutDiscountPricePlain();

        void setMinHeight(@DimenRes int resId);

        void navigateToDealDetailPage(String slug);

        void updateToolbarTitle(String toolbarTitle);

        void updateCheckoutButtonText(String checkoutButtonText);

        void renderSkipToCheckoutMenu();

        boolean isAlreadyCollapsByUser();

        void showPromoOnlyForTopUpAndBillMessage();
    }

    interface Presenter extends DigitalBaseContract.Presenter<View>{

        void onDealsCheckout();

        void autoCollapseCheckoutView();

        void onExpandCollapseButtonView();

        void onNewSelectedDeal(DealProductViewModel viewModel);

        void onDealRemoved(DealProductViewModel viewModel);

        void onSkipMenuClicked();

        void onDealDetailClicked(DealProductViewModel productViewModel);
    }
}
