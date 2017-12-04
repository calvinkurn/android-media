package com.tokopedia.digital.tokocash.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.apiservices.tokocash.WalletService;
import com.tokopedia.core.router.digitalmodule.sellermodule.PeriodRangeModelCore;
import com.tokopedia.core.router.digitalmodule.sellermodule.TokoCashRouter;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.adapter.FilterTokoCashAdapter;
import com.tokopedia.digital.tokocash.adapter.HistoryTokoCashAdapter;
import com.tokopedia.digital.tokocash.domain.HistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.domain.IHistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.interactor.ITokoCashHistoryInteractor;
import com.tokopedia.digital.tokocash.interactor.TokoCashHistoryInteractor;
import com.tokopedia.digital.tokocash.listener.TokoCashHistoryListener;
import com.tokopedia.digital.tokocash.model.HeaderHistory;
import com.tokopedia.digital.tokocash.model.ItemHistory;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;
import com.tokopedia.digital.tokocash.presenter.ITokoCashHistoryPresenter;
import com.tokopedia.digital.tokocash.presenter.TokoCashHistoryPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 8/21/17.
 */

public class HistoryTokocashActivity extends BasePresenterActivity<ITokoCashHistoryPresenter>
        implements TokoCashHistoryListener {

    private static final int SELECTION_TYPE_PERIOD_DATE = 0;
    private static final int EXTRA_INTENT_DATE_PICKER = 50;
    private static final String FORMAT_DATE = "dd+MMM+yyyy";
    private static final String ALL_TRANSACTION_TYPE = "all";
    private static final String EXTRA_START_DATE = "EXTRA_START_DATE";
    private static final String EXTRA_END_DATE = "EXTRA_END_DATE";
    private static final String EXTRA_SELECTION_PERIOD = "EXTRA_SELECTION_PERIOD";
    private static final String EXTRA_SELECTION_TYPE = "EXTRA_SELECTION_TYPE";
    private static final String STATE_DATA_UPDATED = "state_data_updated";
    private static final String STATE_SAVED = "saved";

    @BindView(R2.id.date_label_view)
    LinearLayout layoutDate;
    @BindView(R2.id.text_view_date)
    TextView tvDate;
    @BindView(R2.id.filter_history_tokocash)
    RecyclerView filterHistoryRecyclerView;
    @BindView(R2.id.history_tokocash)
    RecyclerView historyListRecyclerView;
    @BindView(R2.id.view_empty_no_history)
    RelativeLayout viewEmptyNoHistory;
    @BindView(R2.id.main_content)
    LinearLayout mainContent;
    @BindView(R2.id.loading_history)
    ProgressBar loadingHistory;
    @BindView(R2.id.text_empty_page)
    TextView emptyTextTransaction;
    @BindView(R2.id.icon_empty_page)
    ImageView emptyIconTransaction;
    @BindView(R2.id.desc_empty_page)
    TextView emptyDescTransaction;
    @BindView(R2.id.root_view)
    CoordinatorLayout rootView;
    @BindView(R2.id.waiting_transaction_view)
    LinearLayout waitingTransactionView;
    @BindView(R2.id.nested_scroll_view)
    NestedScrollView nestedScrollView;


    private int datePickerSelection = 2;
    private int datePickerType = 0;
    private String typeFilterSelected = "";
    private String startDateFormatted = "";
    private String endDateFormatted = "";
    private boolean isLoadMore;
    private long startDate;
    private long endDate;
    private FilterTokoCashAdapter adapterFilter;
    private HistoryTokoCashAdapter adapterHistory;
    private EndlessRecyclerviewListener endlessRecyclerviewListener;
    private CompositeSubscription compositeSubscription;
    private SnackbarRetry messageSnackbar;
    private RefreshHandler refreshHandler;
    private String stateDataAfterFilter = "";
    private TokoCashHistoryData tokoCashHistoryData;

    @SuppressWarnings("unused")
    @DeepLink(Constants.Applinks.WALLET_TRANSACTION_HISTORY)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return HistoryTokocashActivity.newInstance(context);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, HistoryTokocashActivity.class);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_DATA_UPDATED, stateDataAfterFilter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.stateDataAfterFilter = savedInstanceState.getString(STATE_DATA_UPDATED);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stateDataAfterFilter.equals("")) {
            refreshHandler.startRefresh();
        }
        presenter.getWaitingTransaction();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        SessionHandler sessionHandler = new SessionHandler(this);
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        IHistoryTokoCashRepository repository = new HistoryTokoCashRepository(
                new WalletService(sessionHandler.getAccessTokenTokoCash()));
        ITokoCashHistoryInteractor interactor = new TokoCashHistoryInteractor(repository,
                compositeSubscription,
                new JobExecutor(),
                new UIThread());
        presenter = new TokoCashHistoryPresenter(interactor, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_tokocash;
    }

    @Override
    protected void initView() {
        toolbar.setTitle(getString(R.string.title_menu_history_tokocash));
        initialRangeDateFilter();
        initialFilterRecyclerView();
        initialHistoryRecyclerView();
        refreshHandler = new RefreshHandler(this, getWindow().getDecorView().getRootView(),
                getRefreshHandlerListener());
    }

    private RefreshHandler.OnRefreshHandlerListener getRefreshHandlerListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                if (refreshHandler.isRefreshing()) {
                    presenter.getInitHistoryTokoCash(typeFilterSelected, startDateFormatted, endDateFormatted);
//                    endlessRecyclerviewListener.resetState();
                }
            }
        };
    }

    private void initialRangeDateFilter() {
        Application application = HistoryTokocashActivity.this.getApplication();
        if (application != null && application instanceof TokoCashRouter) {
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
            startCalendar.add(Calendar.DATE, -29);
            startDate = startCalendar.getTimeInMillis();
            endDate = endCalendar.getTimeInMillis();
            String getFormattedDatePicker =
                    ((TokoCashRouter) application)
                            .getRangeDateFormatted(HistoryTokocashActivity.this, startDate, endDate);
            tvDate.setText(getFormattedDatePicker);
        }
    }

    private void initialHistoryRecyclerView() {
        adapterHistory = new HistoryTokoCashAdapter(new ArrayList<ItemHistory>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        endlessRecyclerviewListener = new EndlessRecyclerviewListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//
//            }
//        };
        historyListRecyclerView.setHasFixedSize(true);
        historyListRecyclerView.setLayoutManager(linearLayoutManager);
//        historyListRecyclerView.addOnScrollListener(endlessRecyclerviewListener);
        historyListRecyclerView.setNestedScrollingEnabled(false);
        historyListRecyclerView.setAdapter(adapterHistory);
    }

    private void initialFilterRecyclerView() {
        filterHistoryRecyclerView.setHasFixedSize(true);
        filterHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        adapterFilter = new FilterTokoCashAdapter();
        filterHistoryRecyclerView.setAdapter(adapterFilter);
    }

    @NonNull
    private HistoryTokoCashAdapter.ItemHistoryListener getItemHistoryListener() {
        return new HistoryTokoCashAdapter.ItemHistoryListener() {
            @Override
            public void onClickItemHistory(ItemHistory itemHistory) {
                startActivity(DetailTransactionActivity.newInstance(HistoryTokocashActivity.this,
                        itemHistory));
            }
        };
    }

    @NonNull
    private FilterTokoCashAdapter.FilterTokoCashListener getFilterTokoCashListener() {
        return new FilterTokoCashAdapter.FilterTokoCashListener() {
            @Override
            public void clearFilter() {
                typeFilterSelected = "";
                refreshHandler.startRefresh();
            }

            @Override
            public void selectFilter(String typeFilter) {
                typeFilterSelected = typeFilter;
                presenter.getInitHistoryTokoCash(typeFilter, startDateFormatted, endDateFormatted);
//                endlessRecyclerviewListener.resetState();
            }
        };
    }

    @Override
    protected void setViewListener() {
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount()-1);
                        int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                        if (diff == 0) {
                            adapterHistory.showLoading(true);
                            if (isLoadMore) {
                                presenter.getHistoryLoadMore(typeFilterSelected, startDateFormatted, endDateFormatted);
                            } else {
                                adapterHistory.showLoading(false);
                            }
                        }
                    }
                }
        );
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Application application = HistoryTokocashActivity.this.getApplication();
                if (application != null && application instanceof TokoCashRouter) {
                    Intent intent = ((TokoCashRouter) application)
                            .goToDatePicker(HistoryTokocashActivity.this, getPeriodRangeModel(),
                                    startDate, endDate, datePickerSelection, datePickerType);
                    startActivityForResult(intent, EXTRA_INTENT_DATE_PICKER);
                    stateDataAfterFilter = STATE_SAVED;
                }
            }
        });
    }

    private List<PeriodRangeModelCore> getPeriodRangeModel() {
        List<PeriodRangeModelCore> periodRangeModelCoreList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DATE, 0);
        startCalendar.add(Calendar.DATE, 0);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_today)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -6);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -29);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_thirty_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_this_month)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.MONTH, -1);
        startCalendar.set(Calendar.DATE, 1);
        endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, -1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_last_month)));
        return periodRangeModelCoreList;
    }

    public PeriodRangeModelCore convert(long startDate, long endDate, String label) {
        return new PeriodRangeModelCore(startDate, endDate, label);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EXTRA_INTENT_DATE_PICKER && intent != null) {
            Application application = HistoryTokocashActivity.this.getApplication();
            if (application != null && application instanceof TokoCashRouter) {
                startDate = intent.getLongExtra(EXTRA_START_DATE, -1);
                endDate = intent.getLongExtra(EXTRA_END_DATE, -1);
                datePickerSelection = intent.getIntExtra(EXTRA_SELECTION_PERIOD, 1);
                datePickerType = intent.getIntExtra(EXTRA_SELECTION_TYPE, SELECTION_TYPE_PERIOD_DATE);
                String getFormattedDatePicker =
                        ((TokoCashRouter) application)
                                .getRangeDateFormatted(HistoryTokocashActivity.this,
                                        startDate, endDate);
                tvDate.setText(getFormattedDatePicker);

                startDateFormatted = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(startDate);
                endDateFormatted = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(endDate);
                refreshHandler.startRefresh();
            }
        }
    }

    @Override
    public void renderDataTokoCashHistory(TokoCashHistoryData tokoCashHistoryData, boolean firstTimeLoad) {
        historyListRecyclerView.setVisibility(View.VISIBLE);
        viewEmptyNoHistory.setVisibility(View.GONE);
        adapterHistory.showLoading(false);
        refreshHandler.finishRefresh();
        mainContent.setVisibility(View.VISIBLE);
        adapterFilter.setListener(getFilterTokoCashListener());
        adapterFilter.addFilterTokoCashList(tokoCashHistoryData.getHeaderHistory());
        adapterHistory.setListener(getItemHistoryListener());
        if (firstTimeLoad) {
            adapterHistory.addItemHistoryList(tokoCashHistoryData.getItemHistoryList());
        } else if (isLoadMore) {
            adapterHistory.addItemHistoryListLoadMore(tokoCashHistoryData.getItemHistoryList());
        }
    }

    @Override
    public void renderEmptyTokoCashHistory(List<HeaderHistory> headerHistoryList) {
        historyListRecyclerView.setVisibility(View.GONE);
        viewEmptyNoHistory.setVisibility(View.VISIBLE);
        refreshHandler.finishRefresh();
        for (HeaderHistory header : headerHistoryList) {
            if (header.isSelected()) {
                if (!header.getType().equals(ALL_TRANSACTION_TYPE)) {
                    emptyIconTransaction.setImageDrawable(ContextCompat.getDrawable(this,
                            R.drawable.ic_tokocash_no_history_category));
                    emptyDescTransaction.setText(getString(R.string.desc_empty_page_tokocash));
                    emptyTextTransaction.setText(getString(R.string.message_no_transaction) + " " + header.getName());
                } else {
                    emptyIconTransaction.setImageDrawable(ContextCompat.getDrawable(this,
                            R.drawable.ic_tokocash_no_transaction));
                    emptyDescTransaction.setText(getString(R.string.desc_empty_page_no_transaction_tokocash));
                    emptyTextTransaction.setText(getString(R.string.message_no_transaction));
                }
            }
        }
    }

    @Override
    public void renderErrorMessage(String message) {
        if (messageSnackbar == null) {
            messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(this, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.getHistoryLoadMore(typeFilterSelected, startDateFormatted, endDateFormatted);
                }
            });
        }
        messageSnackbar.showRetrySnackbar();
    }

    @Override
    public void renderEmptyPage(String message) {
        NetworkErrorHelper.showEmptyState(
                getApplicationContext(), rootView, message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                }
        );
    }

    @Override
    public void renderWaitingTransaction(TokoCashHistoryData tokoCashHistory) {
        this.tokoCashHistoryData = tokoCashHistory;
        waitingTransactionView.setVisibility(View.VISIBLE);
        waitingTransactionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(WaitingTransactionActivity.newInstance(tokoCashHistoryData,
                        getApplicationContext()));
            }
        });
    }

    @Override
    public void hideWaitingTransaction() {
        waitingTransactionView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        if (refreshHandler != null && refreshHandler.isRefreshing())
            refreshHandler.finishRefresh();
    }

    @Override
    public void setHasNextPage(boolean hasNextPage) {
        isLoadMore = hasNextPage;
    }

    @Override
    protected void onDestroy() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}