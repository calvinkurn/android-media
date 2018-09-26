package com.tokopedia.saldodetails.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter;
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presentation.listener.SaldoItemListener;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.response.model.Deposit;
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import static android.content.ContentValues.TAG;

public class SaldoDepositFragment extends BaseListFragment<Deposit, SaldoDetailTransactionFactory> implements SaldoDetailContract.View, SaldoItemListener {

    @Inject
    SaldoDetailsPresenter saldoDetailsPresenter;

    TextView totalBalance;
    EditText startDate;
    EditText endDate;
    TextView searchButton;
    TextView drawButton;
    RecyclerView recyclerView;
    View mainView;
    RelativeLayout topSlideOffBar;
    RelativeLayout reviewWarning;
    TextView amountBeingReviewed;
    TextView topupButton;
    FrameLayout saldoFrameLayout;

    DepositScreenListener depositScreenListener;

    SaldoDatePickerUtil datePicker;
    SaldoDepositAdapter adapter;
    RefreshHandler refreshHandler;
    LinearLayoutManager linearLayoutManager;
    Snackbar snackbar;
    private Context context;

    public static SaldoDepositFragment createInstance() {
        return new SaldoDepositFragment();
    }

    protected Bundle savedState;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "ON SAVE INSTANCE STATE");
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b == null) b = new Bundle();
            b.putBundle("internalSavedViewState8954201239547", savedState);
        }
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    private void onSaveState(Bundle state) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_deposit, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialVar();
        initListeners();
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected SaldoDetailTransactionFactory getAdapterTypeFactory() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    private void initViews(View view) {

        totalBalance = view.findViewById(R.id.total_balance);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        searchButton = view.findViewById(R.id.search_button);
        drawButton = view.findViewById(R.id.withdraw_button);
        mainView = view.findViewById(R.id.main_view);
        topSlideOffBar = view.findViewById(R.id.deposit_header);
        reviewWarning = view.findViewById(R.id.review_warning_layout);
        amountBeingReviewed = view.findViewById(R.id.amount_review);
        topupButton = view.findViewById(R.id.topup_button);
        saldoFrameLayout = view.findViewById(R.id.saldo_prioritas_widget);
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        this.recyclerView = super.getRecyclerView(view);
        return super.getRecyclerView(view);
    }

    private void initListeners() {
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ((SaldoDetailsRouter) getActivity().getApplication()).getWithdrawIntent(context);
                saldoDetailsPresenter.onDrawClicked(intent);
                /*TkpdCoreRouter router = (TkpdCoreRouter) getActivity().getApplication();
                presenter.onDrawClicked(router.getWithdrawIntent(context));*/

            }
        });
        startDate.setOnClickListener(onStartDateClicked());
        endDate.setOnClickListener(onEndDateClicked());
        searchButton.setOnClickListener(onSearchClicked());
        recyclerView.addOnScrollListener(onScroll());
        topupButton.setOnClickListener(onTopupSaldoClickedListener(generateTopupUrl()));
    }

    protected void initialVar() {
        datePicker = new SaldoDatePickerUtil(getActivity());
        adapter = new SaldoDepositAdapter(new SaldoDetailTransactionFactory(this));
//        adapter.setOnRetryListenerRV(saldoDetailsPresenter.onRetry());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        /*RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getContext());
        if (remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.SALDO_PRIORITAS_NATIVE_ANDROID,
                true)) {
            presenter.getMerchantSaldoDetails();
        } else {
            hideSaldoPrioritasFragment();
        }*/

    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                saldoDetailsPresenter.loadMore(lastItemPosition, visibleItem);
            }
        };
    }

    private Button.OnClickListener onTopupSaldoClickedListener(final String url) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UnifyTracking.eventDepositTopUp();
                /*Bundle bundle = new Bundle();
                bundle.putString("url", URLGenerator.generateURLSessionLoginV4(url, getActivity()));
                Intent intent = new Intent(context, LoyaltyDetail.class);
                intent.putExtras(bundle);
                context.startActivity(intent);*/
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


    private void populateData() {

    }

    @Override
    protected void initInjector() {

        SaldoDetailsComponent saldoDetailsComponent =
                SaldoDetailsComponentInstance.getComponent(getActivity().getApplication());
        saldoDetailsComponent.inject(this);
        saldoDetailsPresenter.attachView(this);
    }

    private View.OnClickListener onSearchClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saldoDetailsPresenter.onSearchClicked();
            }
        };
    }

    private View.OnClickListener onEndDateClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saldoDetailsPresenter.onEndDateClicked(datePicker);
            }
        };
    }

    private View.OnClickListener onStartDateClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saldoDetailsPresenter.onStartDateClicked(datePicker);
            }
        };
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            onFirstTimeLaunched();
        }
    }

    private void onFirstTimeLaunched() {
        setActionsEnabled(false);
        saldoDetailsPresenter.setFirstDateParameter();
        saldoDetailsPresenter.setCache();
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) b = new Bundle();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        return savedState != null;
    }

    @Override
    public void showProgressLoading() {
        adapter.showLoading();
    }

    @Override
    public void hideProgressLoading() {
        adapter.hideLoading();
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
    public void finishLoading() {
        adapter.hideLoading();
    }

    @Override
    public void showWithdrawalNoPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.error_deposit_no_password_title));
        builder.setMessage(getResources().getString(R.string.error_deposit_no_password_content));
        builder.setPositiveButton(getResources().getString(R.string.error_no_password_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intentToAddPassword(context);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(context, R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(context, R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    private void intentToAddPassword(Context context) {
        context.startActivity(
                ((SaldoDetailsRouter) context.getApplicationContext())
                        .getAddPasswordIntent(context));
    }

    @Override
    public void setBalance(String summaryUseableDepositIdr) {
        totalBalance.setText(summaryUseableDepositIdr);
    }


    @Override
    public SaldoDepositAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setLoading() {
        adapter.showLoading();
    }

    @SuppressLint("Range")
    @Override
    public void showErrorMessage(String error) {
        snackbar = SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_INDEFINITE)
                .setAction("Tutup", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }
                );
        snackbar.show();

    }

    @Override
    public void showHoldWarning(String warningText) {
        reviewWarning.setVisibility(View.VISIBLE);
        amountBeingReviewed.setText(MethodChecker.fromHtml(warningText));
        amountBeingReviewed.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void removeError() {
        snackbar.dismiss();
        adapter.removeErrorNetwork();

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
        saldoDetailsPresenter.onRefresh();
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
                saldoDetailsPresenter.getSummaryDeposit();
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
                saldoDetailsPresenter.getSummaryDeposit();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void showEmptyState(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                saldoDetailsPresenter.getSummaryDeposit();
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
                saldoDetailsPresenter.getSummaryDeposit();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void setTextColor(View view, int colorId) {
        ((TextView) view).setTextColor(colorId);
    }

    @Override
    public void onNotesClicked(String note) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Salin", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager clipBoard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", note);
                clipBoard.setPrimaryClip(clip);
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void onItemClicked(Deposit deposit) {

    }


    public interface DepositScreenListener {
        void showSaldoFragment(int resId, GqlMerchantSaldoDetailsResponse.Details sellerDetails);
    }
}
