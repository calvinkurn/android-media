package com.tokopedia.core.deposit.listener;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.deposit.adapter.DepositAdapter;

/**
 * Created by Nisie on 3/30/16.
 */
public interface DepositFragmentView {
    void setStartDate(String date);
    void setEndDate(String date);

    String getStartDate();

    String getEndDate();

    Context getContext();

    void finishLoading();

    void setBalance(String summaryUseableDepositIdr);

    DepositAdapter getAdapter();

    void setLoading();

    void showErrorMessage(String s);

    void showHoldWarning(String summaryHoldDepositIdr);

    void removeError();

    void hideWarning();

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
}
