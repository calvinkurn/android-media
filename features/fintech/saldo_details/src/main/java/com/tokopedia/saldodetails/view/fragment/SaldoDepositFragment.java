package com.tokopedia.saldodetails.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.design.UserStatusInfoBottomSheet;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;
import com.tokopedia.saldodetails.view.activity.SaldoDepositActivity;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_LOCK;

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

    private long sellerSaldoBalance;
    private long buyerSaldoBalance;
    private long totalSaldoBalance;
    private LinearLayout saldoTypeLL;
    private LinearLayout merchantDetailLL;

    private ImageView saldoDepositExpandIV;
    private ImageView merchantDetailsExpandIV;
    private boolean expandLayout;
    private boolean expandMerchantDetailLayout = true;
    private View merchantCreditFrameLayout;
    private LinearLayout merchantStatusLL;
    private long CHECK_VISIBILITY_DELAY = 700;

    private View layoutTicker;
    private TextView tvTickerMessage;
    private ImageView ivDismissTicker;
    private int mclLateCount = 0;
    private int statusWithDrawLock = -1;
    private static final int MCL_STATUS_ZERO = 0;
    private static final int MCL_STATUS_BLOCK1 = 700;
    private static final int MCL_STATUS_BLOCK2 = 701;
    private static final int MCL_STATUS_BLOCK3 = 999;
    private static final String IS_SELLER = "is_seller";
    private boolean showMclBlockTickerFirebaseFlag = false;
    private FirebaseRemoteConfigImpl remoteConfig;
    private SaveInstanceCacheManager saveInstanceCacheManager;
    public static final String BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID = "merchant_credit_details_id";
    public static final String BUNDLE_PARAM_SELLER_DETAILS_ID = "bundle_param_seller_details_id";


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
        View view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_saldo_deposit, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRemoteConfig();
        initialVar();
        initListeners();
        startShowCase();
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(getContext());
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
                .backgroundContentColorRes(com.tokopedia.design.R.color.black)
                .titleTextColorRes(com.tokopedia.design.R.color.white)
                .textColorRes(com.tokopedia.design.R.color.grey_400)
                .textSizeRes(com.tokopedia.design.R.dimen.sp_12)
                .titleTextSizeRes(com.tokopedia.design.R.dimen.sp_16)
                .nextStringRes(com.tokopedia.design.R.string.intro_seller_saldo_finish_string)
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
                    getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer),
                    getString(com.tokopedia.saldodetails.R.string.saldo_balance_buyer_desc),
                    ShowCaseContentPosition.BOTTOM,
                    Color.WHITE));

            list.add(new ShowCaseObject(
                    sellerSaldoBalanceRL,
                    getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller),
                    getString(com.tokopedia.saldodetails.R.string.saldo_intro_description_seller),
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

        totalBalanceTitle = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_text);
        totalBalanceInfo = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_text_info);

        buyerBalanceInfoIcon = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_buyer_deposit_text_info);
        sellerBalanceInfoIcon = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_seller_deposit_text_info);
        totalBalanceTV = view.findViewById(com.tokopedia.saldodetails.R.id.total_balance);
        drawButton = view.findViewById(com.tokopedia.saldodetails.R.id.withdraw_button);
        topSlideOffBar = view.findViewById(com.tokopedia.saldodetails.R.id.deposit_header);
        holdBalanceLayout = view.findViewById(com.tokopedia.saldodetails.R.id.hold_balance_layout);
        amountBeingReviewed = view.findViewById(com.tokopedia.saldodetails.R.id.amount_review);
        checkBalanceStatus = view.findViewById(com.tokopedia.saldodetails.R.id.check_balance);
        saldoFrameLayout = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_prioritas_widget);
        merchantCreditFrameLayout = view.findViewById(com.tokopedia.saldodetails.R.id.merchant_credit_line_widget);
        tickerMessageRL = view.findViewById(com.tokopedia.saldodetails.R.id.ticker_message_layout);
        tickeRMessageTV = view.findViewById(com.tokopedia.saldodetails.R.id.ticker_message_text);
        tickerMessageCloseButton = view.findViewById(com.tokopedia.saldodetails.R.id.close_ticker_message);
        buyerBalanceTV = view.findViewById(com.tokopedia.saldodetails.R.id.buyer_balance);
        sellerBalanceTV = view.findViewById(com.tokopedia.saldodetails.R.id.seller_balance);
        buyerSaldoBalanceRL = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_buyer_balance_rl);
        sellerSaldoBalanceRL = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_seller_balance_rl);
        saldoBalanceSeparator = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_balance_separator);
        saldoDepositExpandIV = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_layout_expand);
        merchantDetailsExpandIV = view.findViewById(com.tokopedia.saldodetails.R.id.merchant_detail_layout_expand);
        saldoTypeLL = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_type_ll);
        merchantDetailLL = view.findViewById(com.tokopedia.saldodetails.R.id.merchant_details_ll);
        merchantStatusLL = view.findViewById(com.tokopedia.saldodetails.R.id.merchant_status_ll);
        saldoDepositExpandIV.setImageDrawable(MethodChecker.getDrawable(getActivity(), com.tokopedia.design.R.drawable.ic_arrow_up_grey));
        layoutTicker = view.findViewById(com.tokopedia.saldodetails.R.id.layout_holdwithdrawl_dialog);
        tvTickerMessage = view.findViewById(com.tokopedia.design.R.id.tv_desc_info);
        ivDismissTicker = view.findViewById(com.tokopedia.design.R.id.iv_dismiss_ticker);

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

        saldoHistoryFragment = (SaldoTransactionHistoryFragment) getChildFragmentManager().findFragmentById(com.tokopedia.saldodetails.R.id.saldo_history_layout);
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
                Intent intent = RouteManager.getIntent(context, ApplinkConst.INBOX_TICKET);
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
        new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle(getActivity().getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_title))
                .setMessage(getActivity().getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_body))
                .setPositiveButton(getActivity().getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_positive), (dialog, which) -> {
                    Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.SETTING_PROFILE);
                    startActivity(intent);
                    dialog.dismiss();
                })
                .setNegativeButton(getActivity().getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_negative), (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private void goToWithdrawActivity() {
        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getActivity(),
                    ApplinkConstInternalGlobal.WITHDRAW);
            Bundle bundle = new Bundle();
            bundle.putBoolean(IS_SELLER, isSellerEnabled());
            intent.putExtras(bundle);
            saldoDetailsPresenter.onDrawClicked(intent, statusWithDrawLock, mclLateCount, showMclBlockTickerFirebaseFlag);
        }
    }

    private void showSaldoWarningDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle(getActivity().getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_title))
                .setMessage(getActivity().getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_desc))
                .setPositiveButton(
                        getActivity().getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_positiv_button),
                        (dialog, which) -> goToWithdrawActivity())
                .setCancelable(true)
                .show();
    }

    protected void initialVar() {
        saldoDetailsPresenter.setSeller(isSellerEnabled);
        totalBalanceTitle.setText(getResources().getString(com.tokopedia.saldodetails.R.string.total_saldo_text));
        totalBalanceInfo.setVisibility(View.GONE);
        buyerSaldoBalanceRL.setVisibility(View.VISIBLE);
        sellerSaldoBalanceRL.setVisibility(View.VISIBLE);

        totalBalanceInfo.setOnClickListener(v -> showBottomSheetInfoDialog(false));

        buyerBalanceInfoIcon.setOnClickListener(v -> showBottomSheetInfoDialog(false));

        sellerBalanceInfoIcon.setOnClickListener(v -> showBottomSheetInfoDialog(true));


        if (getActivity() != null) {
            if (isSaldoNativeEnabled() && isMerchantCreditLineEnabled()) {
                saldoDetailsPresenter.getUserFinancialStatus();
            } else {

                if (isSaldoNativeEnabled()) {
                    saldoDetailsPresenter.getMerchantSaldoDetails();
                } else {
                    hideSaldoPrioritasFragment();
                }

                if (isMerchantCreditLineEnabled()) {
                    saldoDetailsPresenter.getMerchantCreditLineDetails();
                } else {
                    hideMerchantCreditLineFragment();
                }
            }
        }
    }

    private boolean isSaldoNativeEnabled() {
        return remoteConfig.getBoolean(RemoteConfigKey.SALDO_PRIORITAS_NATIVE_ANDROID,
                true);
    }

    private boolean isMerchantCreditLineEnabled() {
        return remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_MERCHANT_CREDIT_LINE,
                true);
    }

    @Override
    public void hideUserFinancialStatusLayout() {
        merchantStatusLL.setVisibility(View.GONE);
    }

    private void showBottomSheetInfoDialog(boolean isSellerClicked) {
        UserStatusInfoBottomSheet userStatusInfoBottomSheet =
                new UserStatusInfoBottomSheet(context);

        if (isSellerClicked) {
            userStatusInfoBottomSheet.setBody(getResources().getString(com.tokopedia.saldodetails.R.string.saldo_balance_seller_desc));
            userStatusInfoBottomSheet.setTitle(getResources().getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller));
        } else {
            userStatusInfoBottomSheet.setBody(getResources().getString(com.tokopedia.saldodetails.R.string.saldo_balance_buyer_desc));
            userStatusInfoBottomSheet.setTitle(getResources().getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer));
        }

        userStatusInfoBottomSheet.setButtonText(getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_positiv_button));
        userStatusInfoBottomSheet.show();
    }

    @Override
    protected void initInjector() {

        SaldoDetailsComponent saldoDetailsComponent =
                SaldoDetailsComponentInstance.INSTANCE.getComponent(Objects.requireNonNull(getActivity()).getApplication());
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
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getContext());
        showMclBlockTickerFirebaseFlag = remoteConfig.getBoolean(APP_ENABLE_SALDO_LOCK, false);
        saldoDetailsPresenter.getSaldoBalance();
        saldoDetailsPresenter.getTickerWithdrawalMessage();
        saldoDetailsPresenter.getMCLLateCount();
    }

    @Override
    public long getSellerSaldoBalance() {
        return sellerSaldoBalance;
    }

    @Override
    public long getBuyerSaldoBalance() {
        return buyerSaldoBalance;
    }

    @Override
    public void showWithdrawalNoPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(com.tokopedia.saldodetails.R.string.sp_error_deposit_no_password_title));
        builder.setMessage(getResources().getString(com.tokopedia.saldodetails.R.string.sp_error_deposit_no_password_content));
        builder.setPositiveButton(getResources().getString(com.tokopedia.saldodetails.R.string.sp_error_no_password_yes), (dialogInterface, i) -> {
            intentToAddPassword(context);
            dialogInterface.dismiss();
        });
        builder.setNegativeButton(getString(com.tokopedia.saldodetails.R.string.sp_cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    private void intentToAddPassword(Context context) {
        context.startActivity(RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PASSWORD));
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
            drawButton.setTextColor(getResources().getColor(com.tokopedia.design.R.color.black_26));
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
        amountBeingReviewed.setText(String.format(getResources().getString(com.tokopedia.saldodetails.R.string.saldo_hold_balance_text), text));
        amountBeingReviewed.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void hideSaldoPrioritasFragment() {
        saldoFrameLayout.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (merchantCreditFrameLayout.getVisibility() != View.VISIBLE) {
                    hideUserFinancialStatusLayout();
                }
            }
        }, CHECK_VISIBILITY_DELAY);
    }

    @Override
    public void hideMerchantCreditLineFragment() {
        merchantCreditFrameLayout.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (saldoFrameLayout.getVisibility() != View.VISIBLE) {
                    hideUserFinancialStatusLayout();
                }
            }
        }, CHECK_VISIBILITY_DELAY);
    }

    @Override
    public void showTickerMessage(String withdrawalTicker) {
        tickerMessageRL.setVisibility(View.VISIBLE);
        tickeRMessageTV.setText(withdrawalTicker);
    }

    public void showTicker() {

        if (showMclBlockTickerFirebaseFlag) {
            String tickerMsg = getString(com.tokopedia.design.R.string.saldolock_tickerDescription);
            int startIndex = tickerMsg.indexOf(getResources().getString(com.tokopedia.saldodetails.R.string.tickerClickableText));
            String late = Integer.toString(mclLateCount);
            tickerMsg = String.format(getResources().getString(com.tokopedia.design.R.string.saldolock_tickerDescription), late);
            SpannableString ss = new SpannableString(tickerMsg);

            tvTickerMessage.setMovementMethod(LinkMovementMethod.getInstance());

            if (startIndex != -1) {
                ss.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        RouteManager.route(context, String.format("%s?url=%s",
                                ApplinkConst.WEBVIEW, SaldoDetailsConstants.SALDOLOCK_PAYNOW_URL));
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(getResources().getColor(com.tokopedia.design.R.color.tkpd_main_green));
                    }
                }, startIndex - 1, tickerMsg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }


            tvTickerMessage.setText(ss);
            ivDismissTicker.setOnClickListener(v -> layoutTicker.setVisibility(View.GONE));
            layoutTicker.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideTickerMessage() {
        tickerMessageRL.setVisibility(View.GONE);
    }

    @Override
    public void setLateCount(int count) {
        mclLateCount = count;
    }

    @Override
    public void hideWithdrawTicker() {
        layoutTicker.setVisibility(View.GONE);
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
    public void setBuyerSaldoBalance(long balance, String text) {
        buyerSaldoBalance = balance;
        buyerBalanceTV.setText(text);
    }

    @Override
    public void setSellerSaldoBalance(long amount, String formattedAmount) {
        sellerSaldoBalance = amount;
        sellerBalanceTV.setText(formattedAmount);
    }

    @Override
    public void showBuyerSaldoRL() {
        buyerSaldoBalanceRL.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMerchantCreditLineFragment(GqlMerchantCreditResponse response) {
        if (response != null && response.isEligible()) {
            statusWithDrawLock = response.getStatus();
            switch (statusWithDrawLock) {

                case MCL_STATUS_ZERO:
                    hideMerchantCreditLineFragment();
                    break;

                case MCL_STATUS_BLOCK1:
                    showTicker();
                    showMerchantCreditLineWidget(response);
                    break;

                case MCL_STATUS_BLOCK3:
                    showTicker();
                    hideMerchantCreditLineFragment();
                    break;

                default:
                    showMerchantCreditLineWidget(response);
            }
        } else {
            hideMerchantCreditLineFragment();
        }

    }

    public void showMerchantCreditLineWidget(GqlMerchantCreditResponse response) {
        merchantStatusLL.setVisibility(View.VISIBLE);
        Bundle bundle = new Bundle();
        saveInstanceCacheManager = new SaveInstanceCacheManager(context, true);
        saveInstanceCacheManager.put(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS, response);
        if (saveInstanceCacheManager.getId() != null) {
            bundle.putInt(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID, Integer.parseInt(saveInstanceCacheManager.getId()));
        }
        getChildFragmentManager()
                .beginTransaction()
                .replace(com.tokopedia.saldodetails.R.id.merchant_credit_line_widget, MerchantCreditDetailFragment.newInstance(bundle))
                .commit();
    }

    @Override
    public void showSaldoPrioritasFragment(GqlDetailsResponse gqlDetailsResponse) {
        if (gqlDetailsResponse != null &&
                gqlDetailsResponse.isEligible()) {
            merchantStatusLL.setVisibility(View.VISIBLE);
            Bundle bundle = new Bundle();
            saveInstanceCacheManager = new SaveInstanceCacheManager(context, true);
            saveInstanceCacheManager.put(BUNDLE_PARAM_SELLER_DETAILS, gqlDetailsResponse);
            if (saveInstanceCacheManager.getId() != null) {
                bundle.putInt(BUNDLE_PARAM_SELLER_DETAILS_ID, Integer.parseInt(saveInstanceCacheManager.getId()));
            }
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(com.tokopedia.saldodetails.R.id.saldo_prioritas_widget,
                            MerchantSaldoPriorityFragment.Companion.newInstance(bundle))
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
    }

    @Override
    public void showEmptyState() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), () -> saldoDetailsPresenter.getSaldoBalance());
        try {
            View retryLoad = getView().findViewById(com.tokopedia.abstraction.R.id.main_retry);
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

