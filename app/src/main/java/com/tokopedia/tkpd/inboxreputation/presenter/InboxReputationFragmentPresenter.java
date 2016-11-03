package com.tokopedia.tkpd.inboxreputation.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.inboxreputation.adapter.InboxReputationAdapter;

/**
 * Created by Nisie on 21/01/16.
 */
public interface InboxReputationFragmentPresenter {

    void refreshList();

    boolean isLoading();

    void loadMore();

    void getInboxReputation();

    void initData();

    String getFilter();

    boolean isDataEmpty();

    boolean hasNextPage();

    void getNextPage(int itemPosition, int visibleItem);

    boolean isOnLastPosition(int itemPosition, int visibleItem);

    void onDestroyView();

    void setUserVisibleHint(boolean isVisibleToUser);

    void resetPage();

    void onGoToDetailReview(int position);

    void getSingleReview(int position, String invoiceId);

    void generateSearchParam();

}
