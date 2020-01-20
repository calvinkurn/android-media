package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view;

import android.app.Activity;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressView {

    void renderCartData(CartListData cartListData);

    void showInitialLoading();

    void hideInitialLoading();

    void successMakeShipmentData();

    void showError(String message);

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> param);

    void showLoading();

    void hideLoading();

    void showErrorLayout(String message);

    void navigateToCartList();

    Activity getActivityContext();
}
