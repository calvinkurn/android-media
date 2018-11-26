package com.tokopedia.digital.newcart.presentation.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;

import java.util.List;
import java.util.Map;

public interface DigitalCartDealsContract {

    interface View extends CustomerView{

        void showGetCategoriesLoading();

        void hideDealsPage();

        void showGetCategoriesError(String message);

        void hideGetCategoriesLoading();

        void renderDealsTab(List<DealCategoryViewModel> dealCategoryViewModels);

        void showDealsPage();

        List<DealProductViewModel> getSelectedDeals();

        void showErrorInRedSnackbar(@StringRes int resId);

        void notifySelectedDeal();

        DigitalCheckoutPassData getCartPassData();

        CartDigitalInfoData getCartInfoData();

        void showCheckoutView(DigitalCheckoutPassData cartPassData, CartDigitalInfoData cartInfoData);

        void notifySelectedDealsInCheckout(DealProductViewModel viewModel);

        void updateSelectedDeal(int currentFragmentPosition, DealProductViewModel viewModel);

        Map<DealProductViewModel, Integer> getSelectedDealsMap();

        void notifyAdapterInSpecifyFragment(Integer position);

        boolean isOnboardAlreadyShown();

        void showOnboard();

        void notifyCheckoutPageToStartAnimation();
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();

        void onSelectDealProduct(DealProductViewModel viewModel, int currentFragmentPosition);

        void unSelectDealFromCheckoutView(DealProductViewModel viewModel);
    }
}
