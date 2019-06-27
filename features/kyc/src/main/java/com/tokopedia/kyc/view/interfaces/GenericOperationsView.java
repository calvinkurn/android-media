package com.tokopedia.kyc.view.interfaces;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

public interface GenericOperationsView <T> extends CustomerView {
    void success(T data);
    void failure(T data);
    void showHideProgressBar(boolean showProgressBar);
    Activity getActivity();
}
