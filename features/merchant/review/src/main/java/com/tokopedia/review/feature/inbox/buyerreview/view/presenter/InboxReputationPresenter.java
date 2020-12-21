package com.tokopedia.review.feature.inbox.buyerreview.view.presenter;


import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetFilteredInboxReputationSubscriber;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetFirstTimeInboxReputationSubscriber;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetNextPageInboxReputationSubscriber;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.RefreshInboxReputationSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 8/10/17.
 */

public class InboxReputationPresenter
        extends BaseDaggerPresenter<InboxReputation.View>
        implements InboxReputation.Presenter {

    private final GetFirstTimeInboxReputationUseCase getFirstTimeInboxReputationUseCase;
    private final GetInboxReputationUseCase getInboxReputationUseCase;
    private InboxReputation.View viewListener;
    private PagingHandler pagingHandler;

    @Inject
    InboxReputationPresenter(GetFirstTimeInboxReputationUseCase
                                     getFirstTimeInboxReputationUseCase,
                             GetInboxReputationUseCase getInboxReputationUseCase) {
        this.getFirstTimeInboxReputationUseCase = getFirstTimeInboxReputationUseCase;
        this.getInboxReputationUseCase = getInboxReputationUseCase;
        this.pagingHandler = new PagingHandler();
    }

    @Override
    public void attachView(InboxReputation.View view) {
        super.attachView(view);
        this.viewListener = view;

    }

    @Override
    public void getFirstTimeInboxReputation(int tab) {
        viewListener.showLoadingFull();
        getFirstTimeInboxReputationUseCase.execute(
                GetFirstTimeInboxReputationUseCase.getFirstTimeParam(tab),
                new GetFirstTimeInboxReputationSubscriber(viewListener));
    }

    @Override
    public void getNextPage(int lastItemPosition, int visibleItem,
                            String query, String timeFilter,
                            String scoreFilter, int tab) {
        if (hasNextPage() && isOnLastPosition(lastItemPosition,
                visibleItem)) {
            viewListener.showLoadingNext();
            pagingHandler.nextPage();
            getInboxReputationUseCase.execute(
                    GetInboxReputationUseCase.getParam(pagingHandler.getPage(),
                            query,
                            timeFilter,
                            scoreFilter,
                            tab),
                    new GetNextPageInboxReputationSubscriber(viewListener));
        }
    }

    @Override
    public void getFilteredInboxReputation(String query, String timeFilter, String statusFilter, int tab) {
        viewListener.showRefreshing();
        pagingHandler.resetPage();
        getInboxReputationUseCase.execute(
                GetInboxReputationUseCase.getParam(pagingHandler.getPage(),
                        query,
                        timeFilter,
                        statusFilter,
                        tab),
                new GetFilteredInboxReputationSubscriber(viewListener));
    }

    private boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }

    private boolean isOnLastPosition(int itemPosition, int visibleItem) {
        return itemPosition == visibleItem;
    }

    public void refreshPage(String query, String timeFilter, String scoreFilter, int tab) {
        pagingHandler.resetPage();
        getInboxReputationUseCase.execute(
                GetInboxReputationUseCase.getParam(pagingHandler.getPage(), query, timeFilter,
                        scoreFilter, tab),
                new RefreshInboxReputationSubscriber(viewListener, isUsingFilter(query,
                        timeFilter,scoreFilter)));
    }

    private boolean isUsingFilter(String query, String timeFilter, String scoreFilter) {
        return !query.isEmpty() || !timeFilter.isEmpty() || !scoreFilter.isEmpty();
    }

    public void setHasNextPage(boolean hasNextPage) {
        pagingHandler.setHasNext(hasNextPage);
    }

    @Override
    public void detachView() {
        super.detachView();
        getFirstTimeInboxReputationUseCase.unsubscribe();
        getInboxReputationUseCase.unsubscribe();
    }


}
