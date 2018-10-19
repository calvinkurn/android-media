package com.tokopedia.digital.newcart.presentation.contract;

public interface DigitalDealCheckoutContract {
    interface View extends DigitalBaseContract.View{

        boolean isCartDetailViewVisible();

        void hideCartDetailView();

        void showCartDetailView();

        void renderIconToExpand();

        void renderIconToCollapse();
    }

    interface Presenter extends DigitalBaseContract.Presenter<View>{

        void onDealsCheckout();

        void onExpandCollapseButtonView();
    }
}
