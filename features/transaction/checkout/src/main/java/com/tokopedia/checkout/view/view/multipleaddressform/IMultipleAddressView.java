package com.tokopedia.checkout.view.view.multipleaddressform;

import android.app.Activity;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;

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
