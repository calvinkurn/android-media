package com.tokopedia.core.product.listener;

import android.content.Intent;

/**
 * Created by ANGGA on 11/2/2015.
 */
public interface ViewListener {

    void navigateToActivity(Intent intent);

    void showProgressLoading();

    void hideProgressLoading();

    void showToastMessage(String message);
}
