package com.tokopedia.saldodetails.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.design.UserStatusInfoBottomSheet;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

public class SaldoDepositFragment extends BaseDaggerFragment
        implements SaldoDetailContract.View, RefreshHandler.OnRefreshHandlerListener {

    public static final String IS_SELLER_ENABLED = "is_user_enabled";
    public static final String BUNDLE_PARAM_SELLER_DETAILS = "seller_details";

    public static final String BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT = "seller_total_balance_int";
    public static final String BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT = "buyer_total_balance_int";


    private final long SHOW_CASE_DELAY = 400;
    @Inject
    SaldoDetailsPresenter saldoDetailsPresenter;

    @Inject
    UserSession userSession;
    TextView totalBalanceTV;
    /*RelativeLayout startDateLayout;
    RelativeLayout endDateLayout;
    TextView startDateTV;
    TextView endDateTV;*/
    TextView drawButton;
    RelativeLayout mainBalanceRL;

    //    RecyclerView recyclerView;
    RelativeLayout topSlideOffBar;
    RelativeLayout holdBalanceLayout;
    TextView amountBeingReviewed;
    //    TextView depositSummaryTitleTV;
    View saldoFrameLayout;
    SaldoDatePickerUtil datePicker;
    //    SaldoDepositAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    //    Snackbar snackbar;
    LinearLayout tickerMessageRL;
    TextView tickeRMessageTV;
    ImageView tickerMessageCloseButton;


    private RelativeLayout buyerSaldoBalanceRL;
    private RelativeLayout sellerSaldoBalanceRL;
    private TextView buyerBalanceTV;
    private TextView sellerBalanceTV;
    private Context context;
    private TextView checkBalanceStatus;
    //    private boolean isSeller;
    private TextView totalBalanceTitle;
    private View totalBalanceInfo;
    private View buyerBalanceInfoIcon;
    private View sellerBalanceInfoIcon;
    private View saldoBalanceSeparator;
    private boolean isSellerEnabled;
    private boolean isSeller;
    private SaldoTransactionHistoryFragment saldoHistoryFragment;

    private float sellerSaldoBalance;
    private float buyerSaldoBalance;
    private float totalSaldoBalance;

    public static SaldoDepositFragment createInstance(/*boolean isSeller,*/ boolean isSellerEnabled) {
        SaldoDepositFragment saldoDepositFragment = new SaldoDepositFragment();
        Bundle bundle = new Bundle();
//        bundle.putBoolean(IS_USER_SELLER, isSeller);
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

    /*@Override
    public void loadData(int page) {

    }*/

   /* @Override
    protected SaldoDetailTransactionFactory getAdapterTypeFactory() {
        return new SaldoDetailTransactionFactory(this);
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (isVisibleToUser) {
            new Handler().postDelayed(this::setShowCase, SHOW_CASE_DELAY);
        }*/
    }


    @SuppressLint("Range")
    private void initViews(View view) {

        if (getArguments() != null) {
//            isSeller = getArguments().getBoolean(IS_USER_SELLER);
            isSellerEnabled = getArguments().getBoolean(IS_SELLER_ENABLED);
        }
        totalBalanceTitle = view.findViewById(R.id.saldo_deposit_text);
        totalBalanceInfo = view.findViewById(R.id.saldo_deposit_text_info);

        buyerBalanceInfoIcon = view.findViewById(R.id.saldo_buyer_deposit_text_info);
        sellerBalanceInfoIcon = view.findViewById(R.id.saldo_seller_deposit_text_info);

        totalBalanceTV = view.findViewById(R.id.total_balance);
        /*startDateLayout = view.findViewById(R.id.start_date_layout);
        endDateLayout = view.findViewById(R.id.end_date_layout);
        startDateTV = view.findViewById(R.id.start_date_tv);
        endDateTV = view.findViewById(R.id.end_date_tv);*/
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
//        depositSummaryTitleTV = view.findViewById(R.id.deposit_summary_title_tv);
//        dateSelectorLL = view.findViewById(R.id.date_selector_ll);

        buyerBalanceTV = view.findViewById(R.id.buyer_balance);
        sellerBalanceTV = view.findViewById(R.id.seller_balance);
        buyerSaldoBalanceRL = view.findViewById(R.id.saldo_buyer_balance_rl);
        sellerSaldoBalanceRL = view.findViewById(R.id.saldo_seller_balance_rl);
        saldoBalanceSeparator = view.findViewById(R.id.saldo_balance_separator);

        saldoHistoryFragment = (SaldoTransactionHistoryFragment) getChildFragmentManager().findFragmentById(R.id.saldo_history_layout);
    }

    /*@Override
    public RecyclerView getRecyclerView(View view) {
        this.recyclerView = super.getRecyclerView(view);
        return super.getRecyclerView(view);
    }*/

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
        /*startDateLayout.setOnClickListener(onStartDateClicked());
        endDateLayout.setOnClickListener(onEndDateClicked());*/
//        recyclerView.addOnScrollListener(onScroll());
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
        Intent intent = ((SaldoDetailsRouter) getActivity().getApplication()).getWithdrawIntent(context, isSellerEnabled());
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
        isSeller = !TextUtils.isEmpty(userSession.getShopId());
        saldoDetailsPresenter.setSeller(isSellerEnabled);
        if (isSellerEnabled) {
            totalBalanceTitle.setText(getResources().getString(R.string.total_saldo_text));
            totalBalanceInfo.setVisibility(View.GONE);
            buyerSaldoBalanceRL.setVisibility(View.VISIBLE);
            sellerSaldoBalanceRL.setVisibility(View.VISIBLE);
        } else {
            totalBalanceTitle.setText(getResources().getString(R.string.saldo_total_balance_text));
            buyerSaldoBalanceRL.setVisibility(View.GONE);
            sellerSaldoBalanceRL.setVisibility(View.GONE);
            totalBalanceInfo.setVisibility(View.VISIBLE);
        }

        totalBalanceInfo.setOnClickListener(v -> showBottomSheetInfoDialog(false));

        buyerBalanceInfoIcon.setOnClickListener(v -> showBottomSheetInfoDialog(false));

        sellerBalanceInfoIcon.setOnClickListener(v -> showBottomSheetInfoDialog(true));

        datePicker = new SaldoDatePickerUtil(getActivity());
        if (isSellerEnabled && getActivity() != null && getActivity().getApplication() instanceof SaldoDetailsRouter) {

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

    @Override
    public void hideSaldoBalanceSeparator() {
        saldoBalanceSeparator.setVisibility(View.GONE);
    }

    @Override
    public void showSaldoBalanceSeparator() {
        saldoBalanceSeparator.setVisibility(View.VISIBLE);
    }

    private void showBottomSheetInfoDialog(boolean isSellerClicked) {
        UserStatusInfoBottomSheet userStatusInfoBottomSheet =
                new UserStatusInfoBottomSheet(context);

        if (isSellerClicked) {
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

    @Override
    protected void initInjector() {

        SaldoDetailsComponent saldoDetailsComponent =
                SaldoDetailsComponentInstance.getComponent(getActivity().getApplication());
        saldoDetailsComponent.inject(this);
        saldoDetailsPresenter.attachView(this);
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
    public float getSellerSaldoBalance() {
        return sellerSaldoBalance;
    }

    @Override
    public float getBuyerSaldoBalance() {
        return buyerSaldoBalance;
    }

    @Override
    public float getTotalSaldoBalance() {
        return totalSaldoBalance;
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
    public void setBalance(float totalBalance, String summaryUsebleDepositIdr) {
        totalSaldoBalance = totalBalance;
        if (!TextUtils.isEmpty(summaryUsebleDepositIdr)) {
            totalBalanceTV.setText(summaryUsebleDepositIdr);
            totalBalanceTV.setVisibility(View.VISIBLE);
        } else {
            totalBalanceTV.setVisibility(View.GONE);
        }

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

    @SuppressLint("Range")
    @Override
    public void showErrorMessage(String error) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), error);
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
    public boolean isSellerEnabled() {
        return isSellerEnabled;
    }

    @Override
    public void hideSellerSaldoRL() {
        sellerSaldoBalanceRL.setVisibility(View.GONE);
    }

    @Override
    public void showSellerSaldoRL() {
        sellerSaldoBalanceRL.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBuyerSaldoBalance(float balance, String text) {
        buyerSaldoBalance = balance;
        buyerBalanceTV.setText(text);
    }

    @Override
    public void setSellerSaldoBalance(float amount, String formattedAmount) {
        sellerSaldoBalance = amount;
        sellerBalanceTV.setText(formattedAmount);
    }

    @Override
    public void hideBuyerSaldoRL() {
        buyerSaldoBalanceRL.setVisibility(View.GONE);
    }

    @Override
    public void showBuyerSaldoRL() {
        buyerSaldoBalanceRL.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails) {
        if (sellerDetails != null &&
                sellerDetails.isEligible()) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_PARAM_SELLER_DETAILS, sellerDetails);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.saldo_prioritas_widget, MerchantSaldoPriorityFragment.newInstance(bundle))
                    .commit();
        } else {
            hideSaldoPrioritasFragment();
        }
    }

    @Override
    public void hideWarning() {
        holdBalanceLayout.setVisibility(View.GONE);
    }

    @Override
    public void refresh() {
        saldoHistoryFragment.onRefresh();
        saldoDetailsPresenter.onRefresh();
    }

    @Override
    public void showEmptyState() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), () -> saldoDetailsPresenter.getSaldoBalance());
        try {
            View retryLoad = getView().findViewById(R.id.main_retry);
            retryLoad.setTranslationY(topSlideOffBar.getHeight() / 2);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRetry() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), () -> saldoDetailsPresenter.getSaldoBalance()).showRetrySnackbar();
    }

    @Override
    public void setRetry(String error) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error,
                () -> saldoDetailsPresenter.getSaldoBalance()).showRetrySnackbar();
    }

    @Override
    public void onRefresh(View view) {
        saldoDetailsPresenter.onRefresh();
    }

    @Override
    public void onDestroy() {
        saldoDetailsPresenter.detachView();
        super.onDestroy();
    }
}
