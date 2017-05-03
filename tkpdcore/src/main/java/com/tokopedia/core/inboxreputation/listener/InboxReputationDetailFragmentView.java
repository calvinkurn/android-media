package com.tokopedia.core.inboxreputation.listener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.inboxreputation.adapter.InboxReputationDetailAdapter;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;

/**
 * Created by Nisie on 1/25/16.
 */
public interface InboxReputationDetailFragmentView {

    RefreshHandler.OnRefreshHandlerListener onRefresh();

    void finishRefresh();

    Context getContext();

    Activity getActivity();

    boolean getUserVisibleHint();

    void showNoResult();

    void removeNoResult();

    void showLoading();

    void removeLoading();

    InboxReputationDetailAdapter getAdapter();

    void setPullEnabled(boolean b);

    void clearData();

    boolean isRefreshing();

    void showDialogLoading();

    void dismissProgressDialog();

    InboxReputationItem getInboxReputation();

    void showError(String error);

    void onSuccessPostReputation(Bundle resultData);

    void onSuccessSkipReview(Bundle resultData);

    void onSuccessDeleteResponse(Bundle resultData);

    void onSuccessPostReport(Bundle resultData);

    void setActivityResult();

    void showRefresh();

    void setActionEnabled(boolean isEnabled);

    void finishLoading();

    void removeError();

    void showSnackbar(NetworkErrorHelper.RetryClickedListener listener);

    void showSnackbar(String error, NetworkErrorHelper.RetryClickedListener listener);

    void onFailedPostReputation(Bundle resultData);

    void onFailedSkipReview(Bundle resultData);

    void onFailedDeleteResponse(Bundle resultData);

    void onFailedPostReport(Bundle resultData);

    void showShareProvider(InboxReputationDetailItem inboxReputationDetailItem);

    void onSuccessGetDetail(InboxReputationDetail response);
}
