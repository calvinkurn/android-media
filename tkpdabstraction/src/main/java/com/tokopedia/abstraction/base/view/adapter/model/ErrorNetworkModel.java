package com.tokopedia.abstraction.base.view.adapter.model;


import android.support.annotation.IdRes;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.Visitable;

/**
 * @author Kulomady on 1/25/17.
 */

public class ErrorNetworkModel implements Visitable<AdapterTypeFactory> {
    @IdRes
    private int iconDrawableRes;
    private String errorMessage;
    private OnRetryListener onRetryListener;

    @Override
    public int type(AdapterTypeFactory adapterTypeFactory) {
        return adapterTypeFactory.type(this);
    }

    public int getIconDrawableRes() {
        return iconDrawableRes;
    }

    public void setIconDrawableRes(int iconDrawableRes) {
        this.iconDrawableRes = iconDrawableRes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    public OnRetryListener getOnRetryListener() {
        return onRetryListener;
    }

    public interface OnRetryListener{
        void onRetryClicked();
    }
}