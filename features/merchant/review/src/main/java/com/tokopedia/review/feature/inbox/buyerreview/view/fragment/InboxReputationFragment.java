package com.tokopedia.review.feature.inbox.buyerreview.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp;
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.review.R;
import com.tokopedia.review.common.util.ReviewErrorHandler;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking;
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent;
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationDetailActivity;
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationFilterActivity;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.InboxReputationAdapter;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactoryImpl;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;
import com.tokopedia.review.feature.inbox.buyerreview.view.presenter.InboxReputationPresenter;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailPassModel;
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants;
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;


/**
 * @author by nisie on 8/11/17.
 */

public class InboxReputationFragment extends BaseDaggerFragment
        implements InboxReputation.View, SearchInputView.Listener, SellerMigrationReviewViewHolder.SellerMigrationReviewClickListener {

    protected static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);
    public final static String PARAM_TAB = "tab";
    private static final int REQUEST_OPEN_DETAIL = 101;
    private static final int REQUEST_FILTER = 102;
    private static final String ARGS_TIME_FILTER = "ARGS_TIME_FILTER";
    private static final String ARGS_SCORE_FILTER = "ARGS_SCORE_FILTER";
    private static final String ARGS_QUERY = "ARGS_QUERY";

    private SearchInputView searchView;
    private RecyclerView mainList;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayoutManager layoutManager;
    private InboxReputationAdapter adapter;
    private String timeFilter;
    private String scoreFilter;
    private View filterButton;
    private SellerMigrationReviewModel sellerMigrationReviewModel = new SellerMigrationReviewModel();

    @Inject
    InboxReputationPresenter presenter;

    @Inject
    PersistentCacheManager persistentCacheManager;

    @Inject
    ReputationTracking reputationTracking;

    @Inject
    UserSession userSession;

    public static Fragment createInstance(int tab) {
        InboxReputationFragment fragment = new InboxReputationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_TAB, tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION;
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent = ((BaseMainApplication) requireContext().getApplicationContext()).getBaseAppComponent();
        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
                        .builder()
                        .baseAppComponent(baseAppComponent)
                        .build();
        reputationComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initVar(savedInstanceState);
    }

    private void openFilter() {
        Intent intent = InboxReputationFilterActivity.createIntent(getActivity(),
                timeFilter, scoreFilter, getTab());
        startActivityForResult(intent, REQUEST_FILTER);
    }

    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            timeFilter = savedInstanceState.getString(ARGS_TIME_FILTER, "");
            scoreFilter = savedInstanceState.getString(ARGS_SCORE_FILTER, "");
        } else {
            timeFilter = "";
            scoreFilter = "";
        }

        InboxReputationTypeFactory typeFactory = new InboxReputationTypeFactoryImpl(getContext(), this, this);
        adapter = new InboxReputationAdapter(typeFactory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation, container, false);
        mainList = (RecyclerView) parentView.findViewById(R.id.review_list);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        searchView = (SearchInputView) parentView.findViewById(R.id.search);
        searchView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);
        searchView.setListener(this);
        filterButton = parentView.findViewById(R.id.filter_button);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(layoutManager);
        mainList.setAdapter(adapter);

        mainList.addOnScrollListener(onScroll());
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        setQueryHint();

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilter();
            }
        });
    }

    private void setQueryHint() {
        if(getContext() != null) {
            if (getTab() == ReviewInboxConstants.TAB_BUYER_REVIEW) {
                searchView.setSearchHint(getString(R.string.query_hint_review_seller));
            } else {
                searchView.setSearchHint(getString(R.string.query_hint_review_buyer));
            }
        }
    }

    public void refreshPage() {
        if (!swipeToRefresh.isRefreshing())
            showRefreshing();
        presenter.refreshPage(getQuery(),
                timeFilter, scoreFilter, getTab());
    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (!adapter.isLoading() && !adapter.isEmpty())
                    presenter.getNextPage(lastItemPosition, visibleItem,
                            searchView.getSearchText().toString(), timeFilter, scoreFilter, getTab());
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeyboardHandler.hideSoftKeyboard(getActivity());
        if (savedInstanceState != null)
            presenter.getFilteredInboxReputation(
                    savedInstanceState.getString(ARGS_QUERY, ""),
                    savedInstanceState.getString(ARGS_TIME_FILTER, ""),
                    savedInstanceState.getString(ARGS_SCORE_FILTER, ""),
                    getTab()
            );
        else {
            presenter.getFirstTimeInboxReputation(getTab());
        }
    }

    public int getTab() {
        if (getArguments() != null)
            return getArguments().getInt(PARAM_TAB, 1);
        else
            return -1;
    }

    @Override
    public void showLoadingFull() {
        adapter.showLoadingFull();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetFirstTimeInboxReputation(Throwable throwable) {
        if (getActivity() != null & getView() != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), ReviewErrorHandler.getErrorMessage(getContext(), throwable),
                    () -> presenter.getFirstTimeInboxReputation(getTab()));
        }
    }

    @Override
    public void onSuccessGetFirstTimeInboxReputation(InboxReputationUiModel inboxReputationUiModel) {
        searchView.setVisibility(View.VISIBLE);
        filterButton.setVisibility(View.VISIBLE);
        if (!GlobalConfig.isSellerApp() && getTab() == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            adapter.setList(inboxReputationUiModel.getList(), sellerMigrationReviewModel);
        } else {
            adapter.setList(inboxReputationUiModel.getList(), null);
        }
        presenter.setHasNextPage(inboxReputationUiModel.isHasNextPage());
    }

    @Override
    public void finishLoadingFull() {
        adapter.removeLoadingFull();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetNextPage(Throwable throwable) {
        adapter.removeLoading();
        NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                ReviewErrorHandler.getErrorMessage(getContext(), throwable),
                () -> presenter.getFirstTimeInboxReputation(getTab())).showRetrySnackbar();
    }

    @Override
    public void onSuccessGetNextPage(InboxReputationUiModel inboxReputationUiModel) {
        adapter.removeLoading();
        adapter.addList(inboxReputationUiModel.getList());
        presenter.setHasNextPage(inboxReputationUiModel.isHasNextPage());
    }

    @Override
    public void onErrorRefresh(Throwable throwable) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), ReviewErrorHandler.getErrorMessage(getContext(), throwable),
                () -> presenter.refreshPage(getQuery(), timeFilter, scoreFilter, getTab()));
    }

    @Override
    public void onSuccessRefresh(InboxReputationUiModel inboxReputationUiModel) {
        adapter.removeEmpty();
        if (!GlobalConfig.isSellerApp() && getTab() == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            adapter.setList(inboxReputationUiModel.getList(), sellerMigrationReviewModel);
        } else {
            adapter.setList(inboxReputationUiModel.getList());
        }
        presenter.setHasNextPage(inboxReputationUiModel.isHasNextPage());
    }

    @Override
    public void showLoadingNext() {
        adapter.showLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finishLoading() {
        adapter.removeLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGoToDetail(String reputationId, String invoice, String createTime,
                             String revieweeName, String revieweeImage,
                             ReputationDataUiModel reputationDataUiModel, String textDeadline,
                             int adapterPosition, int role) {

        savePassModelToDB(getInboxReputationDetailPassModel(reputationId, invoice, createTime,
                revieweeImage, revieweeName, textDeadline,
                reputationDataUiModel, role));

        startActivityForResult(
                InboxReputationDetailActivity.getCallingIntent(
                        getActivity(),
                        adapterPosition,
                        getTab()),
                REQUEST_OPEN_DETAIL);
    }

    private void savePassModelToDB(InboxReputationDetailPassModel inboxReputationDetailPassModel) {
        if (persistentCacheManager != null) {
            persistentCacheManager.put(
                    InboxReputationDetailActivity.CACHE_PASS_DATA,
                    CacheUtil.convertModelToString(inboxReputationDetailPassModel,
                            new TypeToken<InboxReputationDetailPassModel>() {
                            }.getType())

            );

        }
    }

    private void removeCachePassData() {
        if (persistentCacheManager != null) {
            persistentCacheManager.delete(InboxReputationDetailActivity.CACHE_PASS_DATA);
        }
    }


    private InboxReputationDetailPassModel getInboxReputationDetailPassModel(
            String reputationId,
            String invoice,
            String createTime,
            String revieweeImage,
            String revieweeName,
            String textDeadline,
            ReputationDataUiModel reputationDataUiModel,
            int role) {
        return new InboxReputationDetailPassModel(reputationId, revieweeName, revieweeImage,
                textDeadline, invoice, createTime, reputationDataUiModel, role);

    }

    @Override
    public void showRefreshing() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void onSuccessGetFilteredInboxReputation(InboxReputationUiModel inboxReputationUiModel) {
        adapter.removeEmpty();
        adapter.setList(inboxReputationUiModel.getList());
        presenter.setHasNextPage(inboxReputationUiModel.isHasNextPage());
    }

    @Override
    public void onErrorGetFilteredInboxReputation(Throwable throwable) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), ReviewErrorHandler.getErrorMessage(getContext(), throwable),
                () -> presenter.getFilteredInboxReputation(getQuery(), timeFilter, scoreFilter, getTab())).showRetrySnackbar();
    }

    @Override
    public void finishRefresh() {
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onShowEmpty() {
        searchView.setVisibility(View.GONE);
        filterButton.setVisibility(View.GONE);
        adapter.clearList();
        if (GlobalConfig.isSellerApp()
                || getTab() == ReviewInboxConstants.TAB_BUYER_REVIEW) {
            adapter.showEmpty(getString(R.string.inbox_reputation_seller_empty_title));
        } else {
            adapter.showEmpty(getString(R.string.inbox_reputation_empty_title),
                    getString(R.string.inbox_reputation_empty_button),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToHotlist();
                        }
                    });
        }
        adapter.notifyDataSetChanged();
    }

    private void goToHotlist() {
        RouteManager.route(getContext(), ApplinkConst.HOME);
        getActivity().finish();
    }

    @Override
    public void onShowEmptyFilteredInboxReputation() {
        adapter.clearList();
        adapter.showEmpty(getString(R.string.inbox_reputation_search_empty_title),
                getString(R.string.inbox_reputation_search_empty_button),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeFilter = "";
                        scoreFilter = "";
                        onSearchSubmitted("");
                        searchView.setSearchText("");
                    }
                });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OPEN_DETAIL) {
            removeCachePassData();
            if (resultCode == Activity.RESULT_OK) refreshPage();
        } else if (requestCode == REQUEST_FILTER
                && resultCode == Activity.RESULT_OK
                && data != null) {
            timeFilter = data.getExtras().getString(
                    InboxReputationFilterFragment.SELECTED_TIME_FILTER, "");
            scoreFilter = data.getExtras().getString(InboxReputationFilterFragment
                    .SELECTED_SCORE_FILTER, "");
            presenter.getFilteredInboxReputation(
                    getQuery(),
                    timeFilter,
                    scoreFilter,
                    getTab()
            );
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private String getQuery() {
        if (searchView != null)
            return searchView.getSearchText();
        else
            return "";
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardHandler.DropKeyboard(getActivity(), searchView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_TIME_FILTER, timeFilter);
        outState.putString(ARGS_SCORE_FILTER, scoreFilter);
        outState.putString(ARGS_QUERY, getQuery());
    }

    @Override
    public void onSearchSubmitted(String text) {
        presenter.getFilteredInboxReputation(text,
                timeFilter,
                scoreFilter,
                getTab());
    }

    @Override
    public void onSearchTextChanged(String text) {
        if (text.length() == 0) {
            setQueryHint();
            presenter.getFilteredInboxReputation("",
                    timeFilter,
                    scoreFilter,
                    getTab());
        }
    }

    @Override
    public void onSellerMigrationReviewClicked() {
        Context context = getContext();
        if (context != null) {
            ArrayList<String> appLinks = new ArrayList<>();
            appLinks.add(ApplinkConstInternalSellerapp.SELLER_HOME);
            appLinks.add(ApplinkConst.REPUTATION);
            Intent intent = SellerMigrationActivity.Companion.createIntent(
                    context,
                    SellerMigrationFeatureName.FEATURE_REVIEW_TEMPLATE_AND_STATISTICS,
                    getScreenName(),
                    appLinks);
            startActivity(intent);
        }
    }
}
