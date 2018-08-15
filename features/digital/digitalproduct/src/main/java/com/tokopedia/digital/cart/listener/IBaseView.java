package com.tokopedia.digital.cart.listener;

import android.content.Intent;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public interface IBaseView extends CustomerView {

    void navigateToActivityRequest(Intent intent, int requestCode);

    void navigateToActivity(Intent intent);

    void showInitialProgressLoading();

    void hideInitialProgressLoading();

    void clearContentRendered();

    void showProgressLoading();

    void hideProgressLoading();

    void showToastMessage(String message);

    String getStringFromResource(@StringRes int resId);

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    );

    RequestBodyIdentifier getDigitalIdentifierParam();

    void closeView();

}
