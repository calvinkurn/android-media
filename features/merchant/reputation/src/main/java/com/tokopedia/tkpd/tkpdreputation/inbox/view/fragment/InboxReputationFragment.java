package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.analytic.AppScreen;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTrackingConstant;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationDetailActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFilterActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.bottomsheet.IncentiveOvoBottomSheet;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.ReputationDataViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailPassModel;
import com.tokopedia.unifycomponents.BottomSheetUnify;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;


/**
 * @author by nisie on 8/11/17.
 */

public class InboxReputationFragment extends BaseDaggerFragment
        implements InboxReputation.View, SearchInputView.Listener, SearchInputView.FocusChangeListener {

    protected static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);
    public final static String PARAM_TAB = "tab";
    private static final int REQUEST_OPEN_DETAIL = 101;
    private static final int REQUEST_FILTER = 102;
    private static final int FIRST_TAB_INBOX_REPUTATION = 1;
    private static final String ARGS_TIME_FILTER = "ARGS_TIME_FILTER";
    private static final String ARGS_SCORE_FILTER = "ARGS_SCORE_FILTER";
    private static final String ARGS_QUERY = "ARGS_QUERY";
    private static final String SEE_ALL_REVIEW = "Lihat Semua";

    SearchInputView searchView;
    private RecyclerView mainList;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayoutManager layoutManager;
    private InboxReputationAdapter adapter;
    private String timeFilter;
    private String scoreFilter;
    private View filterButton;
    private boolean isFromWhitespace;
    private Ticker ovoTicker;

    @Inject
    InboxReputationPresenter presenter;

    @Inject
    PersistentCacheManager persistentCacheManager;

    @Inject
    ReputationTracking reputationTracking;

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

        InboxReputationTypeFactory typeFactory = new InboxReputationTypeFactoryImpl(getContext(), this);
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
        ovoTicker = parentView.findViewById(R.id.ovoPointsTicker);
        searchView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);
        searchView.setListener(this);
        searchView.setFocusChangeListener(this);
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
                reputationTracking.onClickButtonFilterReputationTracker(getTab());
            }
        });
    }

    private void setQueryHint() {
        if (getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
            searchView.setSearchHint(getString(R.string.query_hint_review_seller));
        } else {
            searchView.setSearchHint(getString(R.string.query_hint_review_buyer));
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
                reputationTracking.onScrollReviewTracker(getTab());
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
        presenter.getProductIncentiveOvo();
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
        ViewCompat.setNestedScrollingEnabled(mainList, false);
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
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), ErrorHandler.getErrorMessage(getContext(), throwable),
                    () -> presenter.getFirstTimeInboxReputation(getTab()));
        }
    }

    @Override
    public void onSuccessGetFirstTimeInboxReputation(InboxReputationViewModel inboxReputationViewModel) {
        searchView.setVisibility(View.VISIBLE);
        filterButton.setVisibility(View.VISIBLE);
        adapter.setList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
    }

    @Override
    public void onErrorGetProductRevIncentiveOvo(Throwable throwable) {
        ovoTicker.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetProductRevIncentiveOvo(ProductRevIncentiveOvoDomain productRevIncentiveOvoDomain) {
        String title = productRevIncentiveOvoDomain.getProductrevIncentiveOvo().getTicker().getTitle();
        String subtitle = productRevIncentiveOvoDomain.getProductrevIncentiveOvo().getTicker().getSubtitle();
        ovoTicker.setVisibility(View.VISIBLE);
        ovoTicker.setTickerTitle(title);
        ovoTicker.setHtmlDescription(subtitle);
        ovoTicker.setDescriptionClickEvent(new TickerCallback() {
            @Override
            public void onDescriptionViewClick(@NotNull CharSequence charSequence) {
                BottomSheetUnify bottomSheet = new IncentiveOvoBottomSheet(productRevIncentiveOvoDomain, ReputationTrackingConstant.WAITING_REVIEWED);
                if(getFragmentManager() != null) {
                    bottomSheet.show(getFragmentManager(),IncentiveOvoBottomSheet.Companion.getTAG());
                    bottomSheet.setCloseClickListener(new Function1<View, Unit>() {
                        @Override
                        public Unit invoke(View view) {
                            reputationTracking.onClickDismissIncentiveOvoBottomSheetTracker(ReputationTrackingConstant.WAITING_REVIEWED);
                            bottomSheet.dismiss();
                            return Unit.INSTANCE;
                        }
                    });
                }
                reputationTracking.onClickReadSkIncentiveOvoTracker(title, ReputationTrackingConstant.WAITING_REVIEWED);
            }

            @Override
            public void onDismiss() {
                reputationTracking.onClickDismissIncentiveOvoTracker(title, ReputationTrackingConstant.WAITING_REVIEWED);
            }
        });
        // hit tracking while first time success get gql incentive ovo
        if(getTab() == FIRST_TAB_INBOX_REPUTATION) {
            reputationTracking.onSuccessGetIncentiveOvoTracker(title, ReputationTrackingConstant.WAITING_REVIEWED);
        }
        InboxReputationActivity.tickerTitle = title;
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
                ErrorHandler.getErrorMessage(getContext(), throwable),
                () -> presenter.getFirstTimeInboxReputation(getTab())).showRetrySnackbar();
    }

    @Override
    public void onSuccessGetNextPage(InboxReputationViewModel inboxReputationViewModel) {
        adapter.removeLoading();
        adapter.addList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
    }

    @Override
    public void onErrorRefresh(Throwable throwable) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), ErrorHandler.getErrorMessage(getContext(), throwable),
                () -> presenter.refreshPage(getQuery(), timeFilter, scoreFilter, getTab()));
    }

    @Override
    public void onSuccessRefresh(InboxReputationViewModel inboxReputationViewModel) {
        adapter.removeEmpty();
        adapter.setList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
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
                             ReputationDataViewModel reputationDataViewModel, String textDeadline,
                             int adapterPosition, int role) {

        if(reputationDataViewModel.getActionMessage().equals(SEE_ALL_REVIEW)) {
            reputationTracking.seeAllReviewItemOnClickTracker(
                    invoice,
                    (adapterPosition + 1),
                    isFromWhitespace,
                    getTab()
            );
        } else {
            reputationTracking.reviewItemOnClickTracker(
                    invoice,
                    (adapterPosition + 1),
                    isFromWhitespace,
                    getTab()
            );
        }

        savePassModelToDB(getInboxReputationDetailPassModel(reputationId, invoice, createTime,
                revieweeImage, revieweeName, textDeadline,
                reputationDataViewModel, role));

        startActivityForResult(
                InboxReputationDetailActivity.getCallingIntent(
                        getActivity(),
                        adapterPosition,
                        getTab()),
                REQUEST_OPEN_DETAIL);
    }

    @Override
    public void clickFromWhitespace(boolean source) {
        isFromWhitespace = source;
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
            ReputationDataViewModel reputationDataViewModel,
            int role) {
        return new InboxReputationDetailPassModel(reputationId, revieweeName, revieweeImage,
                textDeadline, invoice, createTime, reputationDataViewModel, role);

    }

    @Override
    public void showRefreshing() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void onSuccessGetFilteredInboxReputation(InboxReputationViewModel inboxReputationViewModel) {
        adapter.removeEmpty();
        adapter.setList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
        if(!getQuery().isEmpty())
            reputationTracking.onSuccessFilteredReputationTracker(getQuery(), getTab());
    }

    @Override
    public void onErrorGetFilteredInboxReputation(Throwable throwable) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), ErrorHandler.getErrorMessage(getContext(), throwable),
                () -> presenter.getFilteredInboxReputation(getQuery(), timeFilter, scoreFilter, getTab())).showRetrySnackbar();
        reputationTracking.onErrorFilteredReputationTracker(getQuery(), getTab());
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
                || getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
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
        if(!getQuery().isEmpty())
            reputationTracking.onEmptyFilteredReputationTracker(getQuery(), getTab());
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
    public void onFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            reputationTracking.onClickSearchViewTracker(getTab());
        }
    }
}
