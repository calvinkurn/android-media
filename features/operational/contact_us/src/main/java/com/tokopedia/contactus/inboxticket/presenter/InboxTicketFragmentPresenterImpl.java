package com.tokopedia.contactus.inboxticket.presenter;

import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.core2.R;
import com.tokopedia.contactus.inboxticket.fragment.InboxTicketFragment;
import com.tokopedia.contactus.inboxticket.interactor.InboxTicketCacheInteractor;
import com.tokopedia.contactus.inboxticket.interactor.InboxTicketCacheInteractorImpl;
import com.tokopedia.contactus.inboxticket.interactor.InboxTicketRetrofitInteractor;
import com.tokopedia.contactus.inboxticket.interactor.InboxTicketRetrofitInteractorImpl;
import com.tokopedia.contactus.inboxticket.model.InboxTicketParam;
import com.tokopedia.contactus.inboxticket.model.inboxticket.InboxTicket;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;

import java.util.Map;

/**
 * Created by Nisie on 4/21/16.
 */
public class InboxTicketFragmentPresenterImpl implements InboxTicketFragmentPresenter {

    InboxTicketFragment viewListener;
    InboxTicketRetrofitInteractor networkInteractor;
    InboxTicketCacheInteractor cacheInteractor;
    PagingHandler pagingHandler;
    InboxTicketParam getListParam;

    public InboxTicketFragmentPresenterImpl(InboxTicketFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new InboxTicketRetrofitInteractorImpl();
        this.cacheInteractor = new InboxTicketCacheInteractorImpl();
        this.pagingHandler = new PagingHandler();
        getListParam = new InboxTicketParam();
    }


    @Override
    public RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                doRefresh();
            }
        };
    }

    @Override
    public void getInboxTicket() {
        viewListener.removeError();
        viewListener.setActionEnabled(false);
        showLoading();

        networkInteractor.getInboxTicket(viewListener.getActivity(), getInboxTicketParam(), new InboxTicketRetrofitInteractor.GetInboxTicketListener() {
            @Override
            public void onSuccess(@NonNull InboxTicket result) {
                viewListener.setActionEnabled(true);
                if (pagingHandler.getPage() == 1) {
                    viewListener.getAdapter().getList().clear();
                    if (!isFilterUsed())
                        cacheInteractor.setInboxTicketCache(result);
                }
                viewListener.finishLoading();
                setResult(result);

            }

            @Override
            public void onTimeout(String message) {
                viewListener.finishLoading();
                showError(message);
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                showError(error);

            }

            @Override
            public void onNullData(String error) {
                viewListener.finishLoading();
                if (pagingHandler.getPage() == 1 && viewListener.getAdapter().getList().isEmpty()) {
                    viewListener.getAdapter().showEmptyFull(true);
                } else if (error.equals("")) {
                    showError(viewListener.getString(R.string.default_request_error_null_data));
                } else
                    showError(error);
            }

            @Override
            public void onNoConnectionError() {
                viewListener.finishLoading();

                showError("");

            }
        });
    }

    private boolean isFilterUsed() {
        return !viewListener.getFilter().equals("") && !viewListener.getStatus().equals("");
    }

    private void showError(String message) {
        pagingHandler.setHasNext(false);
        if (viewListener.getAdapter().getList().size() > 0)
            viewListener.showSnackbar(message, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getInboxTicket();
                }
            });
        else {
            viewListener.showEmptyState(message, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    getInboxTicket();
                }
            });
        }
    }

    private void showLoading() {
        if (pagingHandler.getPage() == 1)
            viewListener.showRefreshLoading();
        else
            viewListener.showLoadingBottom();
    }

    private Map<String, String> getInboxTicketParam() {
        getListParam.setPage(String.valueOf(pagingHandler.getPage()));
        return getListParam.getInboxTicketParam();
    }

    @Override
    public void setCache() {
        cacheInteractor.getInboxTicketCache(new InboxTicketCacheInteractor.GetInboxTicketCacheListener() {
            @Override
            public void onSuccess(InboxTicket result) {
                setResult(result);

                getInboxTicket();
            }


            @Override
            public void onError(Throwable e) {
                getInboxTicket();
            }
        });
    }

    private void setResult(InboxTicket result) {
        viewListener.getAdapter().setList(result.getList());

        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.getAdapter().showEmptyFull(true);
        }

        pagingHandler.setHasNext(PagingHandler.CheckHasNext(result.getPaging()));
    }

    @Override
    public void loadMore() {
        if (!isLoading() && pagingHandler.CheckNextPage()) {
            pagingHandler.nextPage();
            getInboxTicket();
        }
    }

    @Override
    public View.OnClickListener onRetry(final boolean isRefresh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInboxTicket();
            }
        };
    }

    @Override
    public boolean isLoading() {
        return networkInteractor.isRequesting();
    }

    @Override
    public boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }

    @Override
    public void onConfirmFilterClicked() {
        pagingHandler.resetPage();
        getListParam.setFilter(viewListener.getFilter());
        getListParam.setKeyword("");
        getListParam.setStatus(viewListener.getStatus());
        getListParam.setPage(String.valueOf(pagingHandler.getPage()));
        getInboxTicket();
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }

    private void doRefresh() {
        if (!isLoading()) {
            pagingHandler.resetPage();
            getInboxTicket();
        } else {
            viewListener.finishRefreshing();
        }
    }

}
