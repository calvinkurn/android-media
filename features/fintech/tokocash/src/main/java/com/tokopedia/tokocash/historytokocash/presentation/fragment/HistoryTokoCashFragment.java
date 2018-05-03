package com.tokopedia.tokocash.historytokocash.presentation.fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.DatePickerTokoCashUtil;
import com.tokopedia.tokocash.historytokocash.presentation.activity.DetailTransactionActivity;
import com.tokopedia.tokocash.historytokocash.presentation.activity.WaitingTransactionActivity;
import com.tokopedia.tokocash.historytokocash.presentation.adapter.HistoryTokoCashAdapter;
import com.tokopedia.tokocash.historytokocash.presentation.contract.TokoCashHistoryContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.HeaderHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.tokocash.historytokocash.presentation.presenter.TokoCashHistoryPresenter;
import com.tokopedia.usecase.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/5/18.
 */

public class HistoryTokoCashFragment extends BaseDaggerFragment implements TokoCashHistoryContract.View {

    private static final int SELECTION_TYPE_PERIOD_DATE = 0;
    private static final int EXTRA_INTENT_DATE_PICKER = 50;
    private static final String FORMAT_DATE = "dd+MMM+yyyy";
    private static final String ALL_TRANSACTION_TYPE = "all";
    private static final String PENDING_TYPE = "pending";

    private static final String EXTRA_START_DATE = "EXTRA_START_DATE";
    private static final String EXTRA_END_DATE = "EXTRA_END_DATE";
    private static final String EXTRA_SELECTION_PERIOD = "EXTRA_SELECTION_PERIOD";
    private static final String EXTRA_SELECTION_TYPE = "EXTRA_SELECTION_TYPE";

    private static final String STATE_DATA_START_DATE = "state_data_start_date";
    private static final String STATE_DATA_END_DATE = "state_data_end_date";
    private static final String STATE_DATA_FILTER_TYPE = "state_data_filter_type";
    private static final String STATE_DATE_PICKER_SELECTION = "date_picker_selection";
    private static final String STATE_DATE_PICKER_TYPE = "date_picker_type";

    private LinearLayout layoutDate;
    private TextView tvDate;
    private QuickSingleFilterView quickSingleFilterHistory;
    private RecyclerView historyListRecyclerView;
    private RelativeLayout viewEmptyNoHistory;
    private LinearLayout mainContent;
    private TextView emptyTextTransaction;
    private ImageView emptyIconTransaction;
    private TextView emptyDescTransaction;
    private CoordinatorLayout rootView;
    private LinearLayout waitingTransactionView;
    private NestedScrollView nestedScrollView;
    private BottomActionView bottomActionView;

    private int datePickerSelection = 2;
    private int datePickerType = 0;
    private String typeFilterSelected = "";
    private String startDateFormatted = "";
    private String endDateFormatted = "";
    private boolean isLoadMore;
    private long startDate;
    private long endDate;
    private HistoryTokoCashAdapter adapterHistory;
    private SnackbarRetry messageSnackbar;
    private RefreshHandler refreshHandler;

    private TokoCashHistoryData tokoCashHistoryData;
    private int oldScrollY = 0;

    @Inject
    TokoCashHistoryPresenter presenter;

    public static HistoryTokoCashFragment newInstance() {
        HistoryTokoCashFragment fragment = new HistoryTokoCashFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_history_tokocash, container, false);
        layoutDate = view.findViewById(R.id.date_label_view);
        tvDate = view.findViewById(R.id.text_view_date);
        quickSingleFilterHistory = view.findViewById(R.id.filter_history_tokocash);
        historyListRecyclerView = view.findViewById(R.id.history_tokocash);
        viewEmptyNoHistory = view.findViewById(R.id.view_empty_no_history);
        mainContent = view.findViewById(R.id.main_content);
        emptyTextTransaction = view.findViewById(R.id.text_empty_page);
        emptyIconTransaction = view.findViewById(R.id.icon_empty_page);
        emptyDescTransaction = view.findViewById(R.id.desc_empty_page);
        rootView = view.findViewById(R.id.root_view);
        waitingTransactionView = view.findViewById(R.id.waiting_transaction_view);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        bottomActionView = view.findViewById(R.id.bottom_action_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector();
        presenter.attachView(this);
        initVar(view, savedInstanceState);
        setActionVar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_DATA_START_DATE, startDate);
        outState.putLong(STATE_DATA_END_DATE, endDate);
        outState.putString(STATE_DATA_FILTER_TYPE, typeFilterSelected);
        outState.putInt(STATE_DATE_PICKER_SELECTION, datePickerSelection);
        outState.putInt(STATE_DATE_PICKER_TYPE, datePickerType);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            this.startDate = savedInstanceState.getLong(STATE_DATA_START_DATE);
            this.endDate = savedInstanceState.getLong(STATE_DATA_END_DATE);
            this.typeFilterSelected = savedInstanceState.getString(STATE_DATA_FILTER_TYPE);
            this.datePickerSelection = savedInstanceState.getInt(STATE_DATE_PICKER_SELECTION);
            this.datePickerType = savedInstanceState.getInt(STATE_DATA_FILTER_TYPE);
            setDateByDateFormatted(startDate, endDate);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshHandler.startRefresh();
        presenter.getWaitingTransaction();
    }

    private void initVar(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initialRangeDateFilter();
        }
        initialHistoryRecyclerView();

        refreshHandler = new RefreshHandler(getActivity(), view,
                new RefreshHandler.OnRefreshHandlerListener() {
                    @Override
                    public void onRefresh(View view) {
                        if (refreshHandler.isRefreshing()) {
                            presenter.getInitHistoryTokoCash();
                        }
                    }
                });
    }

    private void setActionVar() {
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Application application = getActivity().getApplication();
                if (application != null && application instanceof TokoCashRouter) {
                    Intent intent = ((TokoCashRouter) application)
                            .goToDatePicker(getActivity(),
                                    DatePickerTokoCashUtil.getPeriodRangeModel(getActivity()),
                                    startDate, endDate, datePickerSelection, datePickerType);
                    startActivityForResult(intent, EXTRA_INTENT_DATE_PICKER);
                }
            }
        });

        bottomActionView.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestedScrollView.scrollTo(0, 0);
            }
        });

        waitingTransactionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(WaitingTransactionActivity.newInstance(getActivity(), tokoCashHistoryData));
            }
        });

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        showBottomActionWhenScrollingUp();
                        loadMoreContentWhenReachBottom();
                    }
                }
        );
    }

    private void initialRangeDateFilter() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -29);
        startDate = startCalendar.getTimeInMillis();
        endDate = endCalendar.getTimeInMillis();
        setDateByDateFormatted(startDate, endDate);
    }

    private void setDateByDateFormatted(long startDate, long endDate) {
        startDateFormatted = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(startDate);
        endDateFormatted = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(endDate);
        String getFormattedDatePicker = "";
        Application application = getActivity().getApplication();
        if (application != null && application instanceof TokoCashRouter) {
            getFormattedDatePicker =
                    ((TokoCashRouter) application)
                            .getRangeDateFormatted(getActivity(), startDate, endDate);
        }
        tvDate.setText(getFormattedDatePicker);
    }

    private void initialFilterHistory(List<HeaderHistory> filterList) {
        quickSingleFilterHistory.renderFilter(setQuickFilterItems(filterList));
        quickSingleFilterHistory.setListener(new QuickSingleFilterView.ActionListener() {
            @Override
            public void selectFilter(String typeFilter) {
                typeFilterSelected = typeFilter;
                presenter.getInitHistoryTokoCash();
            }
        });
    }

    private List<QuickFilterItem> setQuickFilterItems(List<HeaderHistory> filterList) {
        List<QuickFilterItem> quickFilterItemList = new ArrayList<>();
        for (int i = 0; i < filterList.size(); i++) {
            QuickFilterItem quickFilterItem = new QuickFilterItem();
            quickFilterItem.setName(filterList.get(i).getName());
            quickFilterItem.setType(filterList.get(i).getType());
            quickFilterItem.setSelected(filterList.get(i).isSelected());
            quickFilterItem.setColorBorder(R.color.tkpd_main_green);
            quickFilterItemList.add(quickFilterItem);
        }
        return quickFilterItemList;
    }


    private void initialHistoryRecyclerView() {
        adapterHistory = new HistoryTokoCashAdapter(new ArrayList<ItemHistory>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        historyListRecyclerView.setHasFixedSize(true);
        historyListRecyclerView.setLayoutManager(linearLayoutManager);
        historyListRecyclerView.setNestedScrollingEnabled(false);
        historyListRecyclerView.setAdapter(adapterHistory);
    }

    @NonNull
    private void showBottomActionWhenScrollingUp() {
        if (nestedScrollView.getScrollY() < oldScrollY && nestedScrollView.getScrollY() != 0) {
            bottomActionView.setVisibility(View.VISIBLE);
        } else {
            bottomActionView.setVisibility(View.GONE);
        }
        oldScrollY = nestedScrollView.getScrollY();
    }

    /**
     * Using nestedscrollview because this page contain swipe refresh & endless scroll view
     */
    @NonNull
    private void loadMoreContentWhenReachBottom() {
        View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

        if (diff == 0) {
            if (isLoadMore) {
                presenter.getHistoryLoadMore();
            }
        }
    }

    @Override
    public void hideLoading() {
        if (refreshHandler != null && refreshHandler.isRefreshing())
            refreshHandler.finishRefresh();
    }

    @Override
    public void setHasNextPage(boolean hasNextPage) {
        isLoadMore = hasNextPage;
        adapterHistory.isNextUri(hasNextPage);
    }

    @Override
    public void renderDataTokoCashHistory(TokoCashHistoryData tokoCashHistoryData, boolean firstTimeLoad) {
        mainContent.setVisibility(View.VISIBLE);
        layoutDate.setVisibility(View.VISIBLE);
        quickSingleFilterHistory.setVisibility(View.VISIBLE);
        historyListRecyclerView.setVisibility(View.VISIBLE);
        viewEmptyNoHistory.setVisibility(View.GONE);

        refreshHandler.finishRefresh();
        initialFilterHistory(tokoCashHistoryData.getHeaderHistory());
        adapterHistory.setListener(getItemHistoryListener());
        if (firstTimeLoad) {
            adapterHistory.addItemHistoryList(tokoCashHistoryData.getItemHistoryList());
        } else if (isLoadMore) {
            adapterHistory.addItemHistoryListLoadMore(tokoCashHistoryData.getItemHistoryList());
        }
    }

    @NonNull
    private HistoryTokoCashAdapter.ItemHistoryListener getItemHistoryListener() {
        return new HistoryTokoCashAdapter.ItemHistoryListener() {
            @Override
            public void onClickItemHistory(ItemHistory itemHistory) {
                startActivity(DetailTransactionActivity.newInstance(getActivity(), itemHistory));
            }
        };
    }

    @Override
    public void renderEmptyTokoCashHistory(List<HeaderHistory> headerHistoryList) {
        layoutDate.setVisibility(View.VISIBLE);
        quickSingleFilterHistory.setVisibility(View.VISIBLE);
        historyListRecyclerView.setVisibility(View.GONE);
        viewEmptyNoHistory.setVisibility(View.VISIBLE);
        bottomActionView.setVisibility(View.GONE);

        refreshHandler.finishRefresh();
        for (HeaderHistory header : headerHistoryList) {
            if (header.isSelected()) {
                if (!header.getType().equals(ALL_TRANSACTION_TYPE)) {
                    emptyIconTransaction.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.history_ic_no_category));
                    emptyDescTransaction.setText(getString(R.string.desc_empty_page_tokocash));
                    emptyTextTransaction.setText(getString(R.string.message_no_transaction) + " " + header.getName());
                } else {
                    emptyIconTransaction.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.history_ic_no_transaction));
                    emptyDescTransaction.setText(getString(R.string.desc_empty_page_no_transaction_tokocash));
                    emptyTextTransaction.setText(getString(R.string.message_no_transaction));
                }
            }
        }
    }

    @Override
    public void renderErrorMessage(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(getActivity(), throwable);
        if (messageSnackbar == null) {
            messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.getHistoryLoadMore();
                }
            });
        }
        messageSnackbar.showRetrySnackbar();
    }

    @Override
    public void renderEmptyPage(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(getActivity(), throwable);
        NetworkErrorHelper.showEmptyState(
                getActivity(), rootView, message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                }
        );
    }

    @Override
    public void renderWaitingTransaction(TokoCashHistoryData tokoCashHistoryData) {
        this.tokoCashHistoryData = tokoCashHistoryData;
        waitingTransactionView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWaitingTransaction() {
        waitingTransactionView.setVisibility(View.GONE);
    }

    @Override
    public RequestParams getHistoryTokoCashParam(boolean isWaitingTransaction, int page) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetHistoryDataUseCase.TYPE,
                isWaitingTransaction ? PENDING_TYPE : typeFilterSelected);
        requestParams.putString(GetHistoryDataUseCase.START_DATE,
                isWaitingTransaction ? "" : startDateFormatted);
        requestParams.putString(GetHistoryDataUseCase.END_DATE,
                isWaitingTransaction ? "" : endDateFormatted);
        requestParams.putString(GetHistoryDataUseCase.PAGE, String.valueOf(page));
        return requestParams;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EXTRA_INTENT_DATE_PICKER && intent != null) {
            Application application = getActivity().getApplication();
            if (application != null && application instanceof TokoCashRouter) {
                startDate = intent.getLongExtra(EXTRA_START_DATE, -1);
                endDate = intent.getLongExtra(EXTRA_END_DATE, -1);
                datePickerSelection = intent.getIntExtra(EXTRA_SELECTION_PERIOD, 1);
                datePickerType = intent.getIntExtra(EXTRA_SELECTION_TYPE, SELECTION_TYPE_PERIOD_DATE);
                String getFormattedDatePicker =
                        ((TokoCashRouter) application)
                                .getRangeDateFormatted(getActivity(), startDate, endDate);
                tvDate.setText(getFormattedDatePicker);

                startDateFormatted = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(startDate);
                endDateFormatted = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH).format(endDate);
            }
        }
        refreshHandler.startRefresh();
    }

    @Override
    protected void initInjector() {
        getComponent(TokoCashComponent.class).inject(this);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroyPresenter();
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
