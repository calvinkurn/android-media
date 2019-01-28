package com.tokopedia.saldodetails.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.activity.SaldoDepositActivity;
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter;
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.design.UserStatusInfoBottomSheet;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presentation.listener.SaldoItemListener;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.response.model.DepositHistoryList;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;

import javax.inject.Inject;

public class SaldoDepositFragment extends BaseListFragment<DepositHistoryList, SaldoDetailTransactionFactory>
        implements SaldoDetailContract.View, SaldoItemListener, EmptyResultViewHolder.Callback, RefreshHandler.OnRefreshHandlerListener {

    private static final String IS_USER_SELLER = "is_user_seller";
    private static final String IS_SELLER_ENABLED = "is_user_enabled";
    @Inject
    SaldoDetailsPresenter saldoDetailsPresenter;

    @Inject
    UserSession userSession;
    TextView totalBalance;
    RelativeLayout startDateLayout;
    RelativeLayout endDateLayout;
    TextView startDateTV;
    TextView endDateTV;
    TextView drawButton;
    RelativeLayout mainBalanceRL;
    LinearLayout dateSelectorLL;
    RecyclerView recyclerView;
    RelativeLayout topSlideOffBar;
    RelativeLayout holdBalanceLayout;
    TextView amountBeingReviewed;
    TextView depositSummaryTitleTV;
    FrameLayout saldoFrameLayout;
    SaldoDatePickerUtil datePicker;
    SaldoDepositAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    //    Snackbar snackbar;
    LinearLayout tickerMessageRL;
    TextView tickeRMessageTV;
    ImageView tickerMessageCloseButton;

    private Context context;
    private TextView checkBalanceStatus;
    private boolean isSeller;
    private TextView totalBalanceTitle;
    private View totalBalanceInfo;
    private boolean isSellerEnabled;
    private boolean hasShownShowCase = false;

    public static SaldoDepositFragment createInstance(boolean isSeller, boolean isSellerEnabled) {
        SaldoDepositFragment saldoDepositFragment = new SaldoDepositFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_USER_SELLER, isSeller);
        bundle.putBoolean(IS_SELLER_ENABLED, isSellerEnabled);
        saldoDepositFragment.setArguments(bundle);
        return saldoDepositFragment;
    }

    protected Bundle savedState;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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
        return new Bundle();
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
        return new SaldoDetailTransactionFactory(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    private ArrayList<ShowCaseObject> buildShowCase() {

        ArrayList<ShowCaseObject> list = new ArrayList<>();
        if (isSeller && getActivity() instanceof SaldoDepositActivity) {
            list.add(new ShowCaseObject(
                    ((SaldoDepositActivity) getActivity()).getSaldoTabLayout(),
                    getString(R.string.saldo_seller_tab_title),
                    getString(R.string.saldo_detail_intro_desc1),
                    ShowCaseContentPosition.BOTTOM,
                    R.color.white));

            list.add(new ShowCaseObject(
                    mainBalanceRL,
                    getString(R.string.sp_title_withdraw),
                    getString(R.string.saldo_detail_intro_desc2),
                    ShowCaseContentPosition.BOTTOM,
                    R.color.white));

            list.add(new ShowCaseObject(
                    dateSelectorLL,
                    getString(R.string.deposit_summary_title_seller),
                    getString(R.string.saldo_detail_intro_desc3),
                    ShowCaseContentPosition.BOTTOM,
                    R.color.white));

            list.add(new ShowCaseObject(
                    ((SaldoDepositActivity) getActivity()).getBuyerTabView(),
                    getString(R.string.saldo_buyer_tab_title),
                    getString(R.string.saldo_detail_intro_desc4),
                    ShowCaseContentPosition.BOTTOM,
                    R.color.white));

            return list;

        } else {
            return null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(() -> setShowCase(), 400);
        }
    }

    private void setShowCase() {

        ArrayList<ShowCaseObject> list = buildShowCase();
        if (context == null || list == null) {
            return;
        }
        if (!hasShownShowCase && !ShowCasePreference.hasShown(context, SaldoDepositFragment.class.getName())) {
            hasShownShowCase = true;
            createShowCase().show((Activity) context,
                    SaldoDepositFragment.class.getName(),
                    list);
        }
    }

    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .backgroundContentColorRes(R.color.black)
                .titleTextColorRes(R.color.white)
                .textColorRes(R.color.grey_400)
                .textSizeRes(R.dimen.sp_12)
                .titleTextSizeRes(R.dimen.sp_16)
                .nextStringRes(R.string.intro_seller_saldo_finish_string)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }


    @SuppressLint("Range")
    private void initViews(View view) {

        totalBalanceTitle = view.findViewById(R.id.saldo_deposit_text);
        totalBalanceInfo = view.findViewById(R.id.saldo_deposit_text_info);

        totalBalance = view.findViewById(R.id.total_balance);
        startDateLayout = view.findViewById(R.id.start_date_layout);
        endDateLayout = view.findViewById(R.id.end_date_layout);
        startDateTV = view.findViewById(R.id.start_date_tv);
        endDateTV = view.findViewById(R.id.end_date_tv);
        drawButton = view.findViewById(R.id.withdraw_button);
        topSlideOffBar = view.findViewById(R.id.deposit_header);
        holdBalanceLayout = view.findViewById(R.id.hold_balance_layout);
        amountBeingReviewed = view.findViewById(R.id.amount_review);
        checkBalanceStatus = view.findViewById(R.id.check_balance);
        saldoFrameLayout = view.findViewById(R.id.saldo_prioritas_widget);
        tickerMessageRL = view.findViewById(R.id.ticker_message_layout);
        tickeRMessageTV = view.findViewById(R.id.ticker_message_text);
        tickerMessageCloseButton = view.findViewById(R.id.close_ticker_message);
        mainBalanceRL = view.findViewById(R.id.main_balance_rl);
        depositSummaryTitleTV = view.findViewById(R.id.deposit_summary_title_tv);
        dateSelectorLL = view.findViewById(R.id.date_selector_ll);
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        this.recyclerView = super.getRecyclerView(view);
        return super.getRecyclerView(view);
    }

    private void initListeners() {
        drawButton.setOnClickListener(v -> {
            try {
                if (!userSession.isMsisdnVerified()) {
                    showMustVerify();
                } else if (!userSession.hasShownSaldoWithdrawalWarning()) {
                    userSession.setSaldoWithdrawalWaring(true);
                    showSaldoWarningDialog();
                } else {
                    goToWithdrawActivity();
                }
            } catch (Exception e) {

            }

        });

        checkBalanceStatus.setOnClickListener(v -> {
            try {
                Intent intent = ((SaldoDetailsRouter) getActivity().getApplication())
                        .getInboxTicketCallingIntent(context);
                startActivity(intent);
            } catch (Exception e) {

            }
        });

        tickerMessageCloseButton.setOnClickListener(v -> tickerMessageRL.setVisibility(View.GONE));
        startDateLayout.setOnClickListener(onStartDateClicked());
        endDateLayout.setOnClickListener(onEndDateClicked());
        recyclerView.addOnScrollListener(onScroll());
    }

    private void showMustVerify() {
        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.sp_alert_not_verified_yet_title))
                .setMessage(getActivity().getString(R.string.sp_alert_not_verified_yet_body))
                .setPositiveButton(getActivity().getString(R.string.sp_alert_not_verified_yet_positive), (dialog, which) -> {
                    Intent intent = ((SaldoDetailsRouter) getActivity().getApplicationContext())
                            .getProfileSettingIntent(getActivity());
                    startActivity(intent);
                    dialog.dismiss();
                })
                .setNegativeButton(getActivity().getString(R.string.sp_alert_not_verified_yet_negative), (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private void goToWithdrawActivity() {
        Intent intent = ((SaldoDetailsRouter) getActivity().getApplication()).getWithdrawIntent(context);
        saldoDetailsPresenter.onDrawClicked(intent);
    }

    private void showSaldoWarningDialog() {

        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.sp_saldo_withdraw_warning_title))
                .setMessage(getActivity().getString(R.string.sp_saldo_withdraw_warning_desc))
                .setPositiveButton(
                        getActivity().getString(R.string.sp_saldo_withdraw_warning_positiv_button),
                        (dialog, which) -> goToWithdrawActivity())
                .setCancelable(true)
                .show();
    }

    protected void initialVar() {
        if (getArguments() != null) {
            isSeller = getArguments().getBoolean(IS_USER_SELLER);
            isSellerEnabled = getArguments().getBoolean(IS_SELLER_ENABLED);
        }

        saldoDetailsPresenter.setSeller(isSeller);

        if (isSeller) {
            totalBalanceTitle.setText(getResources().getString(R.string.saldo_total_balance_seller));
            depositSummaryTitleTV.setText(getString(R.string.deposit_summary_title_seller));
        } else if (isSellerEnabled) {
            totalBalanceTitle.setText(getResources().getString(R.string.saldo_total_balance_buyer));
            depositSummaryTitleTV.setText(getString(R.string.deposit_summary_title_buyer));
        } else {
            totalBalanceTitle.setText(getResources().getString(R.string.saldo_total_balance_text));
            depositSummaryTitleTV.setText(getString(R.string.deposit_summary_title));
        }

        totalBalanceInfo.setOnClickListener(v -> showBottomSheetInfoDialog());
        datePicker = new SaldoDatePickerUtil(getActivity());
        adapter = new SaldoDepositAdapter(new SaldoDetailTransactionFactory(this));
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        if (isSeller && getActivity() != null && getActivity().getApplication() instanceof SaldoDetailsRouter) {

            if (((SaldoDetailsRouter) getActivity().getApplication())
                    .isSaldoNativeEnabled()) {
                saldoDetailsPresenter.getMerchantSaldoDetails();
            } else {
                hideSaldoPrioritasFragment();
            }
        } else {
            hideSaldoPrioritasFragment();
        }

    }

    private void showBottomSheetInfoDialog() {
        UserStatusInfoBottomSheet userStatusInfoBottomSheet =
                new UserStatusInfoBottomSheet(context);

        if (isSeller) {
            userStatusInfoBottomSheet.setBody(getResources().getString(R.string.saldo_balance_seller_desc));
            userStatusInfoBottomSheet.setTitle(getResources().getString(R.string.saldo_total_balance_seller));
        } else if (isSellerEnabled) {
            userStatusInfoBottomSheet.setBody(getResources().getString(R.string.saldo_balance_buyer_desc));
            userStatusInfoBottomSheet.setTitle(getResources().getString(R.string.saldo_total_balance_buyer));
        } else {
            userStatusInfoBottomSheet.setBody(getResources().getString(R.string.saldo_balance_buyer_desc));
            userStatusInfoBottomSheet.setTitle(getResources().getString(R.string.saldo_total_balance_text));
        }

        userStatusInfoBottomSheet.setButtonText(getString(R.string.sp_saldo_withdraw_warning_positiv_button));
        userStatusInfoBottomSheet.show();
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

    @Override
    protected void initInjector() {

        SaldoDetailsComponent saldoDetailsComponent =
                SaldoDetailsComponentInstance.getComponent(getActivity().getApplication());
        saldoDetailsComponent.inject(this);
        saldoDetailsPresenter.attachView(this);
    }

    private View.OnClickListener onEndDateClicked() {
        return v -> saldoDetailsPresenter.onEndDateClicked(datePicker);
    }

    private View.OnClickListener onStartDateClicked() {
        return v -> saldoDetailsPresenter.onStartDateClicked(datePicker);
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
        saldoDetailsPresenter.getSaldoBalance();
        saldoDetailsPresenter.getTickerWithdrawalMessage();
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) b = new Bundle();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        return savedState != null;
    }

    @Override
    public Visitable getDefaultEmptyViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.sp_empty_state_icon);
        emptyModel.setTitle(getString(R.string.no_saldo_transactions));
        emptyModel.setButtonTitle(getString(R.string.sp_goto_home));
        emptyModel.setCallback(this);
        return emptyModel;
    }

    @Override
    public void setStartDate(String date) {
        startDateTV.setText(date);
    }

    @Override
    public void setEndDate(String date) {
        endDateTV.setText(date);
    }

    @Override
    public String getStartDate() {
        return startDateTV.getText().toString();
    }

    @Override
    public String getEndDate() {
        return endDateTV.getText().toString();
    }

    @Override
    public void finishLoading() {
        adapter.hideLoading();
    }

    @Override
    public void showWithdrawalNoPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.sp_error_deposit_no_password_title));
        builder.setMessage(getResources().getString(R.string.sp_error_deposit_no_password_content));
        builder.setPositiveButton(getResources().getString(R.string.sp_error_no_password_yes), (dialogInterface, i) -> {
            intentToAddPassword(context);
            dialogInterface.dismiss();
        });
        builder.setNegativeButton(getString(R.string.sp_cancel), (dialogInterface, i) -> dialogInterface.dismiss());
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
    public void setBalance(String summaryUsebleDepositIdr) {
        totalBalance.setText(summaryUsebleDepositIdr);
    }


    @Override
    public void setWithdrawButtonState(boolean state) {
        if (state) {
            drawButton.setTextColor(Color.WHITE);
        } else {
            drawButton.setTextColor(getResources().getColor(R.color.black_26));
        }
        drawButton.setEnabled(state);
        drawButton.setClickable(state);
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
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), error);
    }

    @Override
    public void showInvalidDateError(String errorMessage) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showHoldWarning(String text) {
        holdBalanceLayout.setVisibility(View.VISIBLE);
        amountBeingReviewed.setText(String.format(getResources().getString(R.string.saldo_hold_balance_text), text));
        amountBeingReviewed.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void hideSaldoPrioritasFragment() {
        saldoFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTickerMessage(String withdrawalTicker) {
        tickerMessageRL.setVisibility(View.VISIBLE);
        tickeRMessageTV.setText(withdrawalTicker);
    }

    @Override
    public void hideTickerMessage() {
        tickerMessageRL.setVisibility(View.GONE);
    }

    @Override
    public void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails) {
        if (sellerDetails != null &&
                sellerDetails.isEligible()) {

            Bundle bundle = new Bundle();
            bundle.putParcelable("seller_details", sellerDetails);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.saldo_prioritas_widget, MerchantSaldoPriorityFragment.newInstance(bundle))
                    .commit();
        } else {
            hideSaldoPrioritasFragment();
        }
    }

    @Override
    public void removeError() {
        adapter.removeErrorNetwork();
    }

    @Override
    public void hideWarning() {
        holdBalanceLayout.setVisibility(View.GONE);
    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {
        if (!isAdded() || startDateTV == null || endDateTV == null) {
            return;
        }
        startDateLayout.setEnabled(isEnabled);
        endDateLayout.setEnabled(isEnabled);
    }

    @Override
    public void refresh() {
        saldoDetailsPresenter.onRefresh();
    }

    @Override
    public void showEmptyState() {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), () -> saldoDetailsPresenter.getSummaryDeposit());
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
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), () -> saldoDetailsPresenter.getSummaryDeposit()).showRetrySnackbar();
    }

    @Override
    public void showEmptyState(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error,
                () -> saldoDetailsPresenter.getSummaryDeposit());
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
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error,
                () -> saldoDetailsPresenter.getSummaryDeposit()).showRetrySnackbar();
    }

    @Override
    public void setTextColor(View view, int colorId) {
        ((TextView) view).setTextColor(colorId);
    }

    @Override
    public void onEmptyContentItemTextClicked() {

    }

    @Override
    public void onEmptyButtonClicked() {
        Intent intent = ((SaldoDetailsRouter) getActivity().getApplication())
                .getHomeIntent(context);
        startActivity(intent);
    }

    @Override
    public void onRefresh(View view) {
        saldoDetailsPresenter.onRefresh();
    }

    @Override
    public void onItemClicked(DepositHistoryList depositHistoryList) {

    }

    @Override
    public void onDestroy() {
        saldoDetailsPresenter.detachView();
        super.onDestroy();
    }
}
