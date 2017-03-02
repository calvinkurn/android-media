package com.tokopedia.core.tracking.listener;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.tracking.adapter.TrackingHistoryAdapter;
import com.tokopedia.core.tracking.model.tracking.TrackingResponse;

/**
 * Created by Alifa on 10/12/2016.
 */

public interface TrackingFragmentView {

    Context getContext();

    void finishLoading();

    TrackingHistoryAdapter getAdapter();

    void setLoading();

    void showErrorMessage(String errorMessage);

    void removeError();

    void setActionsEnabled(Boolean isEnabled);

    boolean isRefreshing();

    void refresh();

    Activity getActivity();

    String getString(int resId);

    void showEmptyState();

    void setRetry();

    void showEmptyState(String error);

    void setRetry(String error);

    void updateHeaderView(TrackingResponse response);

    void renderTrackingData(TrackingResponse response);
}
