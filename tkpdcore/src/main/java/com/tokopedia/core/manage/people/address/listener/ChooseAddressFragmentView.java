package com.tokopedia.core.manage.people.address.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.manage.people.address.adapter.ChooseAddressAdapter;

/**
 * Created by Alifa on 10/11/2016.
 */

public interface ChooseAddressFragmentView {
    Context getContext();

    void finishLoading();

    ChooseAddressAdapter getAdapter();

    void setLoading();

    void showErrorMessage(String s);

    void removeError();

    void setActionsEnabled(Boolean isEnabled);

    boolean isRefreshing();

    void refresh();

    Activity getActivity();

    String getString(int resId);

    void showRefreshing();

    void showEmptyState();

    void setRetry();

    void showEmptyState(String error);

    void setRetry(String error);

    void startActivityForResult(Intent intent, int requestCode);

    void resetSearch();
}
