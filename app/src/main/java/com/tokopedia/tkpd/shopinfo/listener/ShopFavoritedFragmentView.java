package com.tokopedia.tkpd.shopinfo.listener;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.tkpd.shopinfo.adapter.ShopFavoritedAdapter;


/**
 * Created by Alifa on 10/5/2016.
 */
public interface ShopFavoritedFragmentView {

    Context getContext();

    void finishLoading();

    ShopFavoritedAdapter getAdapter();

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

    boolean isEmpty();
}
