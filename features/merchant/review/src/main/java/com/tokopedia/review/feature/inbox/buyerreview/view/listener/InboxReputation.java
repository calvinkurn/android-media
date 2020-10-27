package com.tokopedia.review.feature.inbox.buyerreview.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.InboxReputationViewModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.ReputationDataViewModel;

/**
 * @author by nisie on 8/10/17.
 */

public interface InboxReputation {
    interface View extends CustomerView {
        void showLoadingFull();

        void onErrorGetFirstTimeInboxReputation(Throwable throwable);

        void onSuccessGetFirstTimeInboxReputation(InboxReputationViewModel inboxReputationViewModel);

        void finishLoadingFull();

        void onErrorGetNextPage(Throwable throwable);

        void onSuccessGetNextPage(InboxReputationViewModel inboxReputationViewModel);

        void onErrorRefresh(Throwable throwable);

        void onSuccessRefresh(InboxReputationViewModel inboxReputationViewModel);

        void showLoadingNext();

        void finishLoading();

        void onGoToDetail(String reputationId, String invoice, String createTime,
                          String revieweeName, String revieweeImage,
                          ReputationDataViewModel reputationDataViewModel, String textDeadline,
                          int adapterPosition, int role);

        void showRefreshing();

        void onSuccessGetFilteredInboxReputation(InboxReputationViewModel inboxReputationViewModel);

        void onErrorGetFilteredInboxReputation(Throwable throwable);

        void finishRefresh();

        void onShowEmpty();

        void onShowEmptyFilteredInboxReputation();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getFirstTimeInboxReputation(int tab);

        void getNextPage(int lastItemPosition, int visibleItem, String query,
                         String timeFilter, String statusFilter, int tab);

        void getFilteredInboxReputation(String query, String timeFilter, String statusFilter, int tab);
    }
}