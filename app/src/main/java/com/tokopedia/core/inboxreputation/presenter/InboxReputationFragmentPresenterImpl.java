package com.tokopedia.core.inboxreputation.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.inboxreputation.InboxReputationConstant;
import com.tokopedia.core.inboxreputation.activity.InboxReputationDetailActivity;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationDetailFragment;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFragment;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractor;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractorImpl;
import com.tokopedia.core.inboxreputation.interactor.InboxReputationRetrofitInteractor;
import com.tokopedia.core.inboxreputation.interactor.InboxReputationRetrofitInteractorImpl;
import com.tokopedia.core.inboxreputation.listener.InboxReputationFragmentView;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputation;
import com.tokopedia.core.inboxreputation.model.param.InboxReputationPass;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcels;

import java.util.Map;

/**
 * Created by Nisie on 21/01/16.
 */
public class InboxReputationFragmentPresenterImpl implements InboxReputationFragmentPresenter,
        InboxReputationConstant {

    InboxReputationFragmentView viewListener;

    private PagingHandler page;
    private InboxReputationRetrofitInteractor retrofitInteractor;
    private CacheInboxReputationInteractor cacheInboxReputationInteractor;
    private String paramKeyword;
    private boolean isShowingNetworkError = false;

    public InboxReputationFragmentPresenterImpl(InboxReputationFragment viewListener) {
        this.viewListener = viewListener;
        this.retrofitInteractor = new InboxReputationRetrofitInteractorImpl();
        this.page = new PagingHandler();
        this.cacheInboxReputationInteractor = new CacheInboxReputationInteractorImpl();
        this.paramKeyword = "";

    }

    @Override
    public void initData() {
        CommonUtils.dumper("NISNIS" + viewListener.getUserVisibleHint() +  " " + isDataEmpty() + " " + isLoading());
        if (viewListener.getUserVisibleHint() && isDataEmpty() && !isLoading()) {
            cacheInboxReputationInteractor.getInboxReputationCache(viewListener.getArguments().getString(PARAM_NAV),
                    new CacheInboxReputationInteractor.GetInboxReputationCacheListener() {
                        @Override
                        public void onSuccess(InboxReputation inboxReputation) {

                            setResult(inboxReputation);

                            getInboxReputation();

                        }

                        @Override
                        public void onError(Throwable e) {
                            getInboxReputation();
                        }
                    });

        }
    }

    private void showLoading() {
        if (viewListener.getAdapter().getListSize() == 0) {
            viewListener.getAdapter().showLoadingFull(true);
        } else if (page.getPage() == 1) {
            viewListener.showRefreshing();
        } else {
            viewListener.getAdapter().showLoading(true);
        }
    }

    @Override
    public String getFilter() {
        if (viewListener.isAllReviewChecked()) {
            return ALL;
        } else {
            return UNREAD;
        }
    }

    @Override
    public boolean isDataEmpty() {
        try {
            return (page.getPage() == 1 && viewListener.getAdapter().getListSize() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean hasNextPage() {
        return page.CheckNextPage();
    }

    @Override
    public void getNextPage(int itemPosition, int visibleItem) {
        if (hasNextPage() && isOnLastPosition(itemPosition, visibleItem) && canLoadMore()) {
            loadMore();
        }
    }

    private boolean canLoadMore() {
        return viewListener.getAdapter().isLoading();
    }

    @Override
    public boolean isOnLastPosition(int itemPosition, int visibleItem) {
        return itemPosition == visibleItem;
    }

    @Override
    public void onDestroyView() {
        retrofitInteractor.unSubscribeObservable();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isDataEmpty() &&!isShowingNetworkError) {
            getInboxReputation();
        }
    }

    @Override
    public void refreshList() {
            resetPage();
            getInboxReputation();
    }

    public boolean isLoading() {
        return retrofitInteractor.isRequesting();
    }

    @Override
    public void loadMore() {
        if (!isLoading() && page.CheckNextPage()) {
            page.nextPage();
            getInboxReputation();
        }
    }

    @Override
    public void getInboxReputation() {
        showLoading();
        viewListener.removeError();
        viewListener.setActionEnabled(false);

        retrofitInteractor.getInboxReputation(viewListener.getActivity(), getInboxReputationParam(),
                new InboxReputationRetrofitInteractor.InboxReputationListener() {
                    @Override
                    public void onSuccess(@NonNull InboxReputation response) {

                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);

                        if (page.getPage() == 1 && !response.getList().isEmpty() && isFilterDefault()) {
                            cacheInboxReputationInteractor.setInboxReputationCache(viewListener.getArguments().getString(PARAM_NAV), response);
                        }

                        setResult(response);

                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getInboxReputation();
                            }
                        });
                    }

                    @Override
                    public void onFailAuth() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getInboxReputation();
                            }
                        });

                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);
                        showError(error, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getInboxReputation();
                            }
                        });
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showNoResult();
                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getInboxReputation();
                            }
                        });
                    }
                });

    }

    private void setResult(InboxReputation response) {
        isShowingNetworkError = false;

        if (page.getPage() == 1) {
            viewListener.clearData();
        }

        page.setHasNext(PagingHandler.CheckHasNext(response.getPaging()));
        if (page.CheckNextPage()) {
            viewListener.getAdapter().showLoading(true);
        } else {
            viewListener.getAdapter().showLoading(false);
        }

        viewListener.getAdapter().addAll(response.getList());

        if (page.getPage() == 1 && response.getList().size() == 0) {
            viewListener.showNoResult();
        }
    }

    private boolean isFilterDefault() {
        return paramKeyword.equals("") && viewListener.getFilter().equals(ALL);
    }

    private Map<String, String> getInboxReputationParam() {
        InboxReputationPass param = new InboxReputationPass();
        param.setPage(page.getPage());
        param.setAct(ACT_GET_REPUTATION);
        param.setNav(viewListener.getArguments().getString(PARAM_NAV));
        param.setKeyword(paramKeyword);
        param.setFilter(getFilter());
        return param.getInboxReputationParam();
    }

    @Override
    public void resetPage() {
        page.resetPage();
    }

    @Override
    public void onGoToDetailReview(int position) {
        Intent intent = new Intent(viewListener.getActivity(), InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(InboxReputationDetailFragment.BUNDLE_POSITION, position);
        bundle.putParcelable(InboxReputationDetailFragment.BUNDLE_INBOX_REPUTATION, Parcels.wrap(viewListener.getAdapter().getList().get(position)));
        intent.putExtras(bundle);
        viewListener.startActivityForResult(intent, TkpdState.RequestCode.CODE_OPEN_DETAIL_REPUTATION);
        UnifyTracking.eventReviewDetail(viewListener.getArguments().getString(PARAM_NAV, "N/A"));
    }

    @Override
    public void getSingleReview(final int position, final String invoiceId) {
        showLoading();
        viewListener.removeError();
        viewListener.setActionEnabled(false);

        retrofitInteractor.getInboxReputation(viewListener.getActivity().getApplicationContext()
                , getSingleReviewParam(invoiceId),
                new InboxReputationRetrofitInteractor.InboxReputationListener() {
                    @Override
                    public void onSuccess(@NonNull final InboxReputation response) {

                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);

                        viewListener.getAdapter().getList().set(position, response.getList().get(0));
                        viewListener.getAdapter().notifyDataSetChanged();

                        if (page.getPage() == 1 && !response.getList().isEmpty() && isFilterDefault()) {
                            cacheInboxReputationInteractor.getInboxReputationCache(viewListener.getArguments().getString(PARAM_NAV),
                                    new CacheInboxReputationInteractor.GetInboxReputationCacheListener() {
                                        @Override
                                        public void onSuccess(InboxReputation inboxReputation) {
                                            inboxReputation.getList().set(position, response.getList().get(0));
                                            cacheInboxReputationInteractor.setInboxReputationCache(viewListener.getArguments().getString(PARAM_NAV), inboxReputation);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }
                                    });
                        }


                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSingleReview(position, invoiceId);
                            }
                        });
                    }

                    @Override
                    public void onFailAuth() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSingleReview(position, invoiceId);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);
                        showError(error, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSingleReview(position, invoiceId);
                            }
                        });

                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSingleReview(position, invoiceId);
                            }
                        });
                    }
                });
    }

    @Override
    public void generateSearchParam() {
        page.resetPage();
        paramKeyword = viewListener.getKeyword();

    }

    private Map<String, String> getSingleReviewParam(String invoiceId) {
        InboxReputationPass param = new InboxReputationPass();
        param.setPage(0);
        param.setAct(ACT_GET_REPUTATION);
        param.setNav(viewListener.getArguments().getString(PARAM_NAV));
        param.setKeyword(invoiceId);
        param.setFilter(getFilter());
        return param.getInboxReputationParam();
    }

    private void showError(String error, NetworkErrorHelper.RetryClickedListener listener) {
        isShowingNetworkError = true;
        if(viewListener.getAdapter().getList().size() == 0){
            viewListener.showEmptyState(error, listener);
        }else {
            viewListener.showSnackbar(error, listener);
        }
    }

    private void showNetworkError(NetworkErrorHelper.RetryClickedListener listener) {
        isShowingNetworkError = true;
        if(viewListener.getAdapter().getList().size() == 0){
            viewListener.showEmptyState(listener);
        } else{
            viewListener.showSnackbar(listener);
        }
    }

}
