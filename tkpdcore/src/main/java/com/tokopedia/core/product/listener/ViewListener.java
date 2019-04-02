package com.tokopedia.core.product.listener;

import android.app.Dialog;
import android.content.Intent;

/**
 * Created by ANGGA on 11/2/2015.
 */
public interface ViewListener {

    void navigateToActivityRequest(Intent intent, int requestCode);

    void navigateToActivity(Intent intent);

    void showProgressLoading();

    void hideProgressLoading();

    void showToastMessage(String message);

    void showDialog(Dialog dialog);

    void dismissDialog(Dialog dialog);

    void closeView();

}
