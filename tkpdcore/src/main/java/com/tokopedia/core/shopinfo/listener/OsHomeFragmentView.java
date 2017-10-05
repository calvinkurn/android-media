package com.tokopedia.core.shopinfo.listener;

import android.app.Activity;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

/**
 * Created by brilliant.oka on 26/04/17.
 */

public interface OsHomeFragmentView {
    void showLoading();

    void setActionsEnabled(boolean isEnabled);

    Activity getActivity();

    void finishLoading();

    void onGetProduct(ProductModel model);

    void showError(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener);

    void showProgressDialog();

    void showError(String error);
}
