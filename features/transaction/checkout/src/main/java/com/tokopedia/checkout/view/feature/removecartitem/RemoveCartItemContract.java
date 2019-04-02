package com.tokopedia.checkout.view.feature.removecartitem;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.view.feature.removecartitem.viewmodel.CartProductHeaderViewModel;
import com.tokopedia.checkout.view.feature.removecartitem.viewmodel.CartProductItemViewModel;

import java.util.List;
import java.util.Map;

/**
 * @author Irfan Khoirul on 24/05/18.
 */
@Deprecated
public interface RemoveCartItemContract {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void showError(String message);

        void renderSuccessDeleteAllCart(String message);

        void renderOnFailureDeleteCart(String message);

        Activity getActivity();

    }

    interface Presenter extends CustomerPresenter<View> {

        void setCartProductItemViewModelList(List<CartItemData> cartItemDataList);

        List<CartProductItemViewModel> getCartProductItemViewModelList();

        void setCartProductHeaderViewModel(CartProductHeaderViewModel cartProductHeaderViewModel);

        CartProductHeaderViewModel getCartProductHeaderViewModel();

        void processRemoveCartItem(List<String> carIds, boolean addToWishlist);

        Map<String, Object> generateCartDataAnalytics(List<String> selectedCartItemIds,
                                                      String enhancedECommerceAction);

    }

}
