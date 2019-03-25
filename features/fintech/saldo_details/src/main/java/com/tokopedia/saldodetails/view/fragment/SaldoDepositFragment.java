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
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.activity.SaldoDepositActivity;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.design.UserStatusInfoBottomSheet;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

public class SaldoDepositFragment extends BaseDaggerFragment
        implements SaldoDetailContract.View {

    public static final String IS_SELLER_ENABLED = "is_user_enabled";
    public static final String BUNDLE_PARAM_SELLER_DETAILS = "seller_details";
    public static final String BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS = "merchant_credit_details";
    private final long animation_duration = 300;

    public static final String BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT = "seller_total_balance_int";
    public static final String BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT = "buyer_total_balance_int";


    private final long SHOW_CASE_DELAY = 400;
    @Inject
    SaldoDetailsPresenter saldoDetailsPresenter;

    @Inject
    UserSession userSession;
    private TextView totalBalanceTV;
    private TextView drawButton;

    private RelativeLayout topSlideOffBar;
    private RelativeLayout holdBalanceLayout;
    private TextView amountBeingReviewed;
    private View saldoFrameLayout;
    private LinearLayout tickerMessageRL;
    private TextView tickeRMessageTV;
    private ImageView tickerMessageCloseButton;


    private RelativeLayout buyerSaldoBalanceRL;
    private RelativeLayout sellerSaldoBalanceRL;
    private TextView buyerBalanceTV;
    private TextView sellerBalanceTV;
    private Context context;
    private TextView checkBalanceStatus;
    private TextView totalBalanceTitle;
    private View totalBalanceInfo;
    private View buyerBalanceInfoIcon;
    private View sellerBalanceInfoIcon;
    private View saldoBalanceSeparator;
    private boolean isSellerEnabled;
    private SaldoTransactionHistoryFragment saldoHistoryFragment;

    private float sellerSaldoBalance;
    private float buyerSaldoBalance;
    private float totalSaldoBalance;
    private LinearLayout saldoTypeLL;
    private LinearLayout merchantDetailLL;

    private ImageView saldoDepositExpandIV;
    private ImageView merchantDetailsExpandIV;
    private boolean expandLayout;
    private boolean expandMerchantDetailLayout = true;
    private View merchantCreditFrameLayout;
    private LinearLayout merchantStatusLL;

    public SaldoDepositFragment() {
    }

    public static SaldoDepositFragment createInstance(boolean isSellerEnabled) {
        SaldoDepositFragment saldoDepositFragment = new SaldoDepositFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_SELLER_ENABLED, isSellerEnabled);
        saldoDepositFragment.setArguments(bundle);
        return saldoDepositFragment;
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
        startShowCase();
    }

    private void startShowCase() {
        new Handler().postDelayed(this::setShowCase, SHOW_CASE_DELAY);
    }

    private void setShowCase() {
        ArrayList<ShowCaseObject> list = buildShowCase();
        if (context == null || list == null) {
            return;
        }
        if (!ShowCasePreference.hasShown(context, SaldoDepositFragment.class.getName())) {
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

    private ArrayList<ShowCaseObject> buildShowCase() {

        ArrayList<ShowCaseObject> list = new ArrayList<>();
        if (isSellerEnabled && getActivity() instanceof SaldoDepositActivity) {
            list.add(new ShowCaseObject(
                    buyerSaldoBalanceRL,
                    getString(R.string.saldo_total_balance_buyer),
                    getString(R.string.saldo_balance_buyer_desc),
                    ShowCaseContentPosition.BOTTOM,
                    Color.WHITE));

            list.add(new ShowCaseObject(
                    sellerSaldoBalanceRL,
                    getString(R.string.saldo_total_balance_seller),
                    getString(R.string.saldo_intro_description_seller),
                    ShowCaseContentPosition.BOTTOM,
                    Color.WHITE));

            return list;

        } else {
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("Range")
    private void initViews(View view) {

        if (getArguments() != null) {
            isSellerEnabled = getArguments().getBoolean(IS_SELLER_ENABLED);
        }

        expandLayout = isSellerEnabled;

        totalBalanceTitle = view.findViewById(R.id.saldo_deposit_text);
        totalBalanceInfo = view.findViewById(R.id.saldo_deposit_text_info);

        buyerBalanceInfoIcon = view.findViewById(R.id.saldo_buyer_deposit_text_info);
        sellerBalanceInfoIcon = view.findViewById(R.id.saldo_seller_deposit_text_info);
        totalBalanceTV = view.findViewById(R.id.total_balance);
        drawButton = view.findViewById(R.id.withdraw_button);
        topSlideOffBar = view.findViewById(R.id.deposit_header);
        holdBalanceLayout = view.findViewById(R.id.hold_balance_layout);
        amountBeingReviewed = view.findViewById(R.id.amount_review);
        checkBalanceStatus = view.findViewById(R.id.check_balance);
        saldoFrameLayout = view.findViewById(R.id.saldo_prioritas_widget);
        merchantCreditFrameLayout = view.findViewById(R.id.merchant_credit_line_widget);
        tickerMessageRL = view.findViewById(R.id.ticker_message_layout);
        tickeRMessageTV = view.findViewById(R.id.ticker_message_text);
        tickerMessageCloseButton = view.findViewById(R.id.close_ticker_message);
        buyerBalanceTV = view.findViewById(R.id.buyer_balance);
        sellerBalanceTV = view.findViewById(R.id.seller_balance);
        buyerSaldoBalanceRL = view.findViewById(R.id.saldo_buyer_balance_rl);
        sellerSaldoBalanceRL = view.findViewById(R.id.saldo_seller_balance_rl);
        saldoBalanceSeparator = view.findViewById(R.id.saldo_balance_separator);
        saldoDepositExpandIV = view.findViewById(R.id.saldo_deposit_layout_expand);
        merchantDetailsExpandIV = view.findViewById(R.id.merchant_detail_layout_expand);
        saldoTypeLL = view.findViewById(R.id.saldo_type_ll);
        merchantDetailLL = view.findViewById(R.id.merchant_details_ll);
        merchantStatusLL = view.findViewById(R.id.merchant_status_ll);
        saldoDepositExpandIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_grey));

        if (expandLayout) {
            saldoTypeLL.setVisibility(View.VISIBLE);
        } else {
            saldoDepositExpandIV.animate().rotation(180).setDuration(animation_duration);
            saldoTypeLL.setVisibility(View.GONE);
        }

        if (expandMerchantDetailLayout) {
            merchantDetailLL.setVisibility(View.VISIBLE);
        } else {
            merchantDetailsExpandIV.animate().rotation(180).setDuration(animation_duration);
            merchantDetailLL.setVisibility(View.GONE);
        }

        saldoHistoryFragment = (SaldoTransactionHistoryFragment) getChildFragmentManager().findFragmentById(R.id.saldo_history_layout);
    }

    private void initListeners() {

        saldoDepositExpandIV.setOnClickListener(v -> {
            if (expandLayout) {
                saldoDepositExpandIV.animate().rotation(180).setDuration(animation_duration);
                expandLayout = false;
                collapse(saldoTypeLL);
            } else {
                saldoDepositExpandIV.animate().rotation(0).setDuration(animation_duration);
                expandLayout = true;
                expand(saldoTypeLL);
            }

        });

        merchantDetailsExpandIV.setOnClickListener(v -> {
            if (expandMerchantDetailLayout) {
                merchantDetailsExpandIV.animate().rotation(180).setDuration(animation_duration);
                expandMerchantDetailLayout = false;
                collapse(merchantDetailLL);
            } else {
                merchantDetailsExpandIV.animate().rotation(0).setDuration(animation_duration);
                expandMerchantDetailLayout = true;
                expand(merchantDetailLL);
            }
        });

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
                Intent intent = ((SaldoDetailsRouter) Objects.requireNonNull(getActivity()).getApplication())
                        .getInboxTicketCallingIntent(context);
                startActivity(intent);
            } catch (Exception e) {

            }
        });

        tickerMessageCloseButton.setOnClickListener(v -> tickerMessageRL.setVisibility(View.GONE));
    }

    private void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    private void showMustVerify() {
        new android.support.v7.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()))
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
        if (getActivity() != null) {
            Intent intent = ((SaldoDetailsRouter) getActivity().getApplication()).getWithdrawIntent(context, isSellerEnabled());
            saldoDetailsPresenter.onDrawClicked(intent);
        }
    }

    private void showSaldoWarningDialog() {
        new android.support.v7.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle(getActivity().getString(R.string.sp_saldo_withdraw_warning_title))
                .setMessage(getActivity().getString(R.string.sp_saldo_withdraw_warning_desc))
                .setPositiveButton(
                        getActivity().getString(R.string.sp_saldo_withdraw_warning_positiv_button),
                        (dialog, which) -> goToWithdrawActivity())
                .setCancelable(true)
                .show();
    }

    protected void initialVar() {
        saldoDetailsPresenter.setSeller(isSellerEnabled);

        totalBalanceTitle.setText(getResources().getString(R.string.total_saldo_text));
        totalBalanceInfo.setVisibility(View.GONE);
        buyerSaldoBalanceRL.setVisibility(View.VISIBLE);
        sellerSaldoBalanceRL.setVisibility(View.VISIBLE);

        totalBalanceInfo.setOnClickListener(v -> showBottomSheetInfoDialog(false));

        buyerBalanceInfoIcon.setOnClickListener(v -> showBottomSheetInfoDialog(false));

        sellerBalanceInfoIcon.setOnClickListener(v -> showBottomSheetInfoDialog(true));

        if (getActivity() != null && getActivity().getApplication() instanceof SaldoDetailsRouter) {

            if (((SaldoDetailsRouter) getActivity().getApplication())
                    .isSaldoNativeEnabled() && ((SaldoDetailsRouter) getActivity().getApplication())
                    .isMerchantCreditLineEnabled()) {
                saldoDetailsPresenter.getUserFinancialStatus();
            } else {

                if (((SaldoDetailsRouter) getActivity().getApplication())
                        .isSaldoNativeEnabled()) {
                    saldoDetailsPresenter.getMerchantSaldoDetails();
                } else {
                    hideSaldoPrioritasFragment();
                }

                if (((SaldoDetailsRouter) getActivity().getApplication())
                        .isMerchantCreditLineEnabled()) {
                    saldoDetailsPresenter.getMerchantCreditLineDetails();
                } else {
                    hideMerchantCreditLineFragment();
                }
            }
        } else {
            hideUserFinancialStatusLayout();
        }

    }

    @Override
    public void hideUserFinancialStatusLayout() {
        merchantStatusLL.setVisibility(View.GONE);
        hideSaldoPrioritasFragment();
        hideMerchantCreditLineFragment();
    }

    private void showBottomSheetInfoDialog(boolean isSellerClicked) {
        UserStatusInfoBottomSheet userStatusInfoBottomSheet =
                new UserStatusInfoBottomSheet(context);

        if (isSellerClicked) {
            userStatusInfoBottomSheet.setBody(getResources().getString(R.string.saldo_balance_seller_desc));
            userStatusInfoBottomSheet.setTitle(getResources().getString(R.string.saldo_total_balance_seller));
        } else {
            userStatusInfoBottomSheet.setBody(getResources().getString(R.string.saldo_balance_buyer_desc));
            userStatusInfoBottomSheet.setTitle(getResources().getString(R.string.saldo_total_balance_buyer));
        }

        userStatusInfoBottomSheet.setButtonText(getString(R.string.sp_saldo_withdraw_warning_positiv_button));
        userStatusInfoBottomSheet.show();
    }

    @Override
    protected void initInjector() {

        SaldoDetailsComponent saldoDetailsComponent =
                SaldoDetailsComponentInstance.getComponent(Objects.requireNonNull(getActivity()).getApplication());
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
        onFirstTimeLaunched();
    }

    private void onFirstTimeLaunched() {
        saldoDetailsPresenter.getSaldoBalance();
        saldoDetailsPresenter.getTickerWithdrawalMessage();
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
    public void setBalance(long totalBalance, String summaryUsebleDepositIdr) {
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
    public void hideMerchantCreditLineFragment() {
        merchantCreditFrameLayout.setVisibility(View.GONE);
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
    public void showBuyerSaldoRL() {
        buyerSaldoBalanceRL.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails) {
        if (sellerDetails != null &&
                sellerDetails.isEligible()) {
            merchantStatusLL.setVisibility(View.VISIBLE);
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
    public void showMerchantCreditLineFragment(GqlMerchantCreditResponse response) {
        if (response != null && response.isEligible()) {
            merchantStatusLL.setVisibility(View.VISIBLE);
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS, response);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.merchant_credit_line_widget, MerchantCreditDetailFragment.newInstance(bundle))
                    .commit();
        } else {
            hideMerchantCreditLineFragment();
        }

    }

    @Override
    public void hideWarning() {
        holdBalanceLayout.setVisibility(View.GONE);
    }

    @Override
    public void refresh() {
        saldoHistoryFragment.onRefresh();
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
    public void onDestroy() {
        saldoDetailsPresenter.detachView();
        super.onDestroy();
    }
}
