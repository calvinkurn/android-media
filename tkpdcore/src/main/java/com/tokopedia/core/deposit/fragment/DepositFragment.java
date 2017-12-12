package com.tokopedia.core.deposit.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.deposit.adapter.DepositAdapter;
import com.tokopedia.core.deposit.listener.DepositFragmentView;
import com.tokopedia.core.deposit.presenter.DepositFragmentPresenter;
import com.tokopedia.core.deposit.presenter.DepositFragmentPresenterImpl;
import com.tokopedia.core.loyaltysystem.LoyaltyDetail;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RefreshHandler;

import butterknife.BindView;

/**
 * Created by Nisie on 3/30/16.
 */
public class DepositFragment extends BasePresenterFragment<DepositFragmentPresenter>
        implements DepositFragmentView {

    @BindView(R2.id.total_balance)
    TextView totalBalance;

    @BindView(R2.id.start_date)
    EditText startDate;

    @BindView(R2.id.end_date)
    EditText endDate;

    @BindView(R2.id.search_button)
    TextView searchButton;

    @BindView(R2.id.withdraw_button)
    TextView drawButton;

    @BindView(R2.id.balance_list)
    RecyclerView listViewBalance;

    @BindView(R2.id.main_view)
    View mainView;

    @BindView(R2.id.deposit_header)
    RelativeLayout topSlideOffBar;

    @BindView(R2.id.review_warning_layout)
    RelativeLayout reviewWarning;

    @BindView(R2.id.amount_review)
    TextView amountBeingReviewed;

    @BindView(R2.id.topup_button)
    TextView topupButton;

    DatePickerUtil datePicker;
    DepositAdapter adapter;
    RefreshHandler refreshHandler;
    LinearLayoutManager linearLayoutManager;
    Snackbar snackbar;

    public static DepositFragment createInstance() {
        DepositFragment fragment = new DepositFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        setActionsEnabled(false);
        presenter.setFirstDateParameter();
        presenter.setCache();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new DepositFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_deposit;
    }

    @Override
    protected void initView(View view) {
        this.refreshHandler = new RefreshHandler(getActivity(), view, onRefresh());
        snackbar = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_INDEFINITE);

    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefresh();
            }
        };
    }

    @Override
    protected void setViewListener() {
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDrawClicked();
            }
        });
        startDate.setOnClickListener(onStartDateClicked());
        endDate.setOnClickListener(onEndDateClicked());
        searchButton.setOnClickListener(onSearchClicked());
        listViewBalance.addOnScrollListener(onScroll());
        topupButton.setOnClickListener(onTopupSaldoClickedListener(generateTopupUrl()));
    }


    private Button.OnClickListener onTopupSaldoClickedListener(final String url) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventDepositTopUp();
                Bundle bundle = new Bundle();
                bundle.putString("url", URLGenerator.generateURLSessionLoginV4(url,getActivity()));
                Intent intent = new Intent(context, LoyaltyDetail.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };
    }


    private String generateTopupUrl() {
        return "https://pulsa.tokopedia.com/" +
                "saldo/" +
                "?utm_source=android" +
                "&utm_medium=link" +
                "&utm_campaign=top%20up%20saldo";
    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                presenter.loadMore(lastItemPosition, visibleItem);
            }
        };
    }

    private View.OnClickListener onSearchClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSearchClicked();
            }
        };
    }

    private View.OnClickListener onEndDateClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEndDateClicked(datePicker);
            }
        };
    }

    private View.OnClickListener onStartDateClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onStartDateClicked(datePicker);
            }
        };
    }

    @Override
    protected void initialVar() {
        datePicker = new DatePickerUtil(getActivity());
        adapter = DepositAdapter.createInstance(getActivity());
        adapter.setOnRetryListenerRV(presenter.onRetry());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listViewBalance.setLayoutManager(linearLayoutManager);
        listViewBalance.setAdapter(adapter);
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void setStartDate(String date) {
        startDate.setText(date);
    }

    @Override
    public void setEndDate(String date) {
        endDate.setText(date);
    }

    @Override
    public String getStartDate() {
        return startDate.getText().toString();
    }

    @Override
    public String getEndDate() {
        return endDate.getText().toString();
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void finishLoading() {
        adapter.showLoading(false);
        adapter.showEmpty(false);
        refreshHandler.setRefreshing(false);

    }

    @Override
    public void setBalance(String summaryUseableDepositIdr) {
        totalBalance.setText(summaryUseableDepositIdr);
    }

    @Override
    public DepositAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setLoading() {
        adapter.showLoading(true);
    }

    @Override
    public void showErrorMessage(String error) {
        snackbar = SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_close), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }
                );
        snackbar.show();

    }

    @Override
    public void showHoldWarning(String summaryHoldDepositIdr) {
        reviewWarning.setVisibility(View.VISIBLE);
        amountBeingReviewed.setText(MethodChecker.fromHtml(getString(R.string.message_deposit_review) + " <b>" + summaryHoldDepositIdr + "</b>" + " " + getString(R.string.message_time_deposit_return)));
    }

    @Override
    public void removeError() {
        snackbar.dismiss();
        adapter.showEmpty(false);

    }

    @Override
    public void hideWarning() {
        reviewWarning.setVisibility(View.GONE);
    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {
        if (!isAdded() || startDate == null || endDate == null || drawButton == null || searchButton == null) {
            return;
        }
        startDate.setEnabled(isEnabled);
        endDate.setEnabled(isEnabled);
        drawButton.setEnabled(isEnabled);
        searchButton.setEnabled(isEnabled);
    }

    @Override
    public boolean isRefreshing() {
        return refreshHandler.isRefreshing();
    }

    @Override
    public void refresh() {
        presenter.onRefresh();
    }

    @Override
    public void showRefreshing() {
        refreshHandler.setRefreshing(true);
        refreshHandler.setIsRefreshing(true);
    }

    @Override
    public void showEmptyState() {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getSummaryDeposit();
            }
        });
        try {
            View retryLoad = getView().findViewById(R.id.main_retry);
            retryLoad.setTranslationY(topSlideOffBar.getHeight() / 2);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRetry() {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getSummaryDeposit();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void showEmptyState(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getSummaryDeposit();
            }
        });
        try {
            View retryLoad = getView().findViewById(R.id.main_retry);
            retryLoad.setTranslationY(topSlideOffBar.getHeight() / 2);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRetry(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getSummaryDeposit();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
