package com.tokopedia.core.rescenter.inbox.listener;

import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.rescenter.inbox.model.ResCenterInboxItem;
import com.tokopedia.core.rescenter.inbox.model.ResolutionList;

import java.util.ArrayList;

/**
 * Created on 3/23/16.
 */
public interface InboxResCenterView {

    void setBottomSheetDialog();

    View getRootView();

    void setRootView(View rootView);

    void prepareRecyclerView();

    void setRefreshing(boolean param);

    void saveCurrentInboxList(Bundle state);

    void restorePreviousInboxList(Bundle savedState);

    void addInboxItem(ArrayList<ResolutionList> list);

    void addLoading();

    void removeLoading();

    void addHeaderItem(ResCenterInboxItem headerItem);

    void removeHeaderItem();

    void addNoResult();

    void removeNoResult();

    void addRetryItemOnHeader();

    void addRetryItemOnBottom();

    void removeRetryItem();

    void setRetryView();

    void showCache(ArrayList<ResolutionList> list);

    void replaceCache(ArrayList<ResolutionList> list);

    void prepareBottomSheet();

    void clearCurrentList();

    void setPullToRefreshAble(boolean isAble);

    void setFilterAble(boolean isAble);

    void showMessageErrorEmptyState(String message);

    ArrayList<ResCenterInboxItem> getList();

    void showMessageErrorSnackBar(String message);

    void showTimeOutEmtpyState(NetworkErrorHelper.RetryClickedListener retryClickedListener);
}
