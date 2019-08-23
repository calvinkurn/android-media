package com.tokopedia.checkout.view.common.base;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;

/**
 * @author anggaprasetiyo on 1/9/17.
 */

public interface IBaseView extends CustomerView {

    void navigateToActivityRequest(Intent intent, int requestCode);

    void navigateToActivity(Intent intent);

    void showProgressLoading();

    void hideProgressLoading();

    void showToastMessage(String message);

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    );
}
