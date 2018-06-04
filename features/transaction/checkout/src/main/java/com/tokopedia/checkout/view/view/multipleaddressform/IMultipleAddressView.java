package com.tokopedia.checkout.view.view.multipleaddressform;

import android.app.Activity;

import com.tokopedia.abstraction.common.utils.TKPDMapParam; /**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressView {

    void successMakeShipmentData();

    void showError();

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> param);

    void showLoading();

    void hideLoading();

    Activity getActivity();
}
