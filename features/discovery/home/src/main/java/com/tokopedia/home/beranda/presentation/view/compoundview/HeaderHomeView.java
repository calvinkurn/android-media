package com.tokopedia.home.beranda.presentation.view.compoundview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.gamification.util.HexValidator;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.data.model.SectionContentItem;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.tokocash.tracker.WalletAnalytics;

/**
 * @author anggaprasetiyo on 11/12/17.
 */

public class HeaderHomeView extends BaseCustomView {

    private static final String CDN_URL = "https://ecs7.tokopedia.net/img/android/";
    private static final String BG_CONTAINER_URL = CDN_URL + "bg_product_fintech_tokopoint_normal/" +
            "drawable-xhdpi/bg_product_fintech_tokopoint_normal.png";

    private static final String TITLE_HEADER_WEBSITE = "TokoPoints";
    private static final String TITLE = "OVO";
    private static final String WALLET_TYPE = "OVO";

    private HomeCategoryListener listener;
    private HeaderViewModel headerViewModel;

    private View view;

    private View scanHolder;

    private View tokoCashHolder;
    private TextView tvTitleTokocash;
    private TextView tvBalanceTokocash;
    private ImageView ivLogoTokocash;
    private TextView tvActionTokocash;
    private ProgressBar tokocashProgressBar;

    private View tokoPointHolder;
    private TextView tvBalanceTokoPoint;
    private TextView tvActionTokopoint;
    private ImageView ivLogoTokoPoint;
    private ProgressBar tokopointProgressBarLayout;
    private LinearLayout tokopointActionContainer;
    private WalletAnalytics walletAnalytics;
    private TextView mTextCouponCount;

    public HeaderHomeView(@NonNull Context context, HeaderViewModel headerViewModel, HomeCategoryListener listener) {
        super(context);
        this.headerViewModel = headerViewModel;
        this.listener = listener;
        notifyHeader();
    }

    public HeaderHomeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        notifyHeader();
    }

    public HeaderHomeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        notifyHeader();
    }

    public void notifyHeader() {
        if (headerViewModel == null)
            return;
        if (listener == null)
            return;

        if (getContext().getApplicationContext() instanceof IHomeRouter) {
            AnalyticTracker analyticTracker = ((IHomeRouter) getContext().getApplicationContext()).getAnalyticTracker();
            walletAnalytics = new WalletAnalytics(analyticTracker);
        }

        if (headerViewModel.isUserLogin()) {
            render();
        } else {
            renderNonLogin();
        }
    }

    public void setListener(HomeCategoryListener listener) {
        this.listener = listener;
    }

    private void renderNonLogin() {
        if (view == null) {
            view = inflate(getContext(), R.layout.layout_item_widget_ovo_tokopoint_nonlogin, this);
            scanHolder = view.findViewById(R.id.container_action_scan);
            View container = view.findViewById(R.id.container_nonlogin);
            ImageView imgNonLogin = view.findViewById(R.id.bg_container_nonlogin);

            ImageHandler.LoadImage(imgNonLogin, BG_CONTAINER_URL);

            container.setOnClickListener(onCheckNowListener());
            scanHolder.setOnClickListener(onScanListener());
        }
    }

    private void render() {
        if (view == null) {
            view = inflate(getContext(), R.layout.layout_item_widget_ovo_tokopoint_login, this);
            scanHolder = view.findViewById(R.id.container_action_scan);
            tokoCashHolder = view.findViewById(R.id.container_tokocash);
            tvActionTokocash = view.findViewById(R.id.tv_btn_action_tokocash);
            tvTitleTokocash = view.findViewById(R.id.tv_title_tokocash);
            tvBalanceTokocash = view.findViewById(R.id.tv_balance_tokocash);
            ivLogoTokocash = view.findViewById(R.id.iv_logo_tokocash);
            tokocashProgressBar = view.findViewById(R.id.progress_bar_tokocash);

            tokoPointHolder = view.findViewById(R.id.container_tokopoint);
            tvBalanceTokoPoint = view.findViewById(R.id.tv_balance_tokopoint);
            tvActionTokopoint = view.findViewById(R.id.tv_btn_action_tokopoint);
            ivLogoTokoPoint = view.findViewById(R.id.iv_logo_tokopoint);
            tokopointProgressBarLayout = view.findViewById(R.id.progress_bar_tokopoint_layout);
            tokopointActionContainer = view.findViewById(R.id.container_action_tokopoint);
            mTextCouponCount = view.findViewById(R.id.text_coupon_count);

            scanHolder.setOnClickListener(onScanListener());
        }
        viewListener();
    }

    private OnClickListener onScanListener() {
        return v -> getContext().startActivity(((IHomeRouter) getContext().getApplicationContext())
                .gotoQrScannerPage(false));
    }

    private OnClickListener onCheckNowListener() {
        return v -> RouteManager.route(getContext(), ApplinkConst.LOGIN);
    }

    private void viewListener() {
        renderTokocashLayoutListener();
        renderTokoPointLayoutListener();
    }

    private void renderTokocashLayoutListener() {
        HomeHeaderWalletAction homeHeaderWalletAction = headerViewModel.getHomeHeaderWalletActionData();
        if (headerViewModel.getHomeHeaderWalletActionData() == null && headerViewModel.isWalletDataError()) {
            tokoCashHolder.setOnClickListener(getOnClickRefreshTokocash());
            tvTitleTokocash.setText(R.string.home_header_tokocash_unable_to_load_label);
            tvActionTokocash.setText(R.string.home_header_tokocash_refresh_label);
            tvActionTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setVisibility(GONE);
            tokocashProgressBar.setVisibility(GONE);
        } else if (headerViewModel.getHomeHeaderWalletActionData() == null && !headerViewModel.isWalletDataError()) {
            tokoCashHolder.setOnClickListener(null);
            tokocashProgressBar.setVisibility(VISIBLE);
        } else {
            if (!TextUtils.isEmpty(homeHeaderWalletAction.getWalletType()) && homeHeaderWalletAction.getWalletType().equals(WALLET_TYPE)) {
                successRenderOvo(homeHeaderWalletAction);
            } else {
                successRenderTokoCash(homeHeaderWalletAction);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void renderTokoPointLayoutListener() {
        if (headerViewModel.getTokopointsDrawerHomeData() == null && headerViewModel.isTokoPointDataError()) {
            tokoPointHolder.setOnClickListener(getOnClickRefreshTokoPoint());
            mTextCouponCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            mTextCouponCount.setVisibility(VISIBLE);
            mTextCouponCount.setText(R.string.home_header_tokopoint_unable_to_load_label);
            mTextCouponCount.setTextColor(getContext().getResources().getColor(R.color.font_black_primary_70));
            tvActionTokopoint.setText(R.string.home_header_tokopoint_refresh_label);
            tvActionTokopoint.setVisibility(VISIBLE);
            tokopointProgressBarLayout.setVisibility(GONE);
            tokopointActionContainer.setVisibility(VISIBLE);
            ivLogoTokoPoint.setImageResource(R.drawable.ic_product_fintech_tokopoint_normal_24);
        } else if (headerViewModel.getTokopointsDrawerHomeData() == null && !headerViewModel.isTokoPointDataError()) {
            tokoPointHolder.setOnClickListener(null);
            tokopointProgressBarLayout.setVisibility(VISIBLE);
            tokopointActionContainer.setVisibility(GONE);
        } else {
            tokopointProgressBarLayout.setVisibility(GONE);
            tokopointActionContainer.setVisibility(VISIBLE);
            tvActionTokopoint.setVisibility(GONE);
            tvBalanceTokoPoint.setVisibility(VISIBLE);
            mTextCouponCount.setVisibility(VISIBLE);

            ImageHandler.loadImageAndCache(ivLogoTokoPoint, headerViewModel.getTokopointsDrawerHomeData().getIconImageURL());
            mTextCouponCount.setTypeface(mTextCouponCount.getTypeface(), Typeface.BOLD);
            if (headerViewModel.getTokopointsDrawerHomeData().getSectionContent() != null
                    && headerViewModel.getTokopointsDrawerHomeData().getSectionContent().size() > 0) {
                setTokopointsHeaderData(headerViewModel.getTokopointsDrawerHomeData().getSectionContent().get(0), tvBalanceTokoPoint);
                if (headerViewModel.getTokopointsDrawerHomeData().getSectionContent().size() >= 2) {
                    setTokopointsHeaderData(headerViewModel.getTokopointsDrawerHomeData().getSectionContent().get(1), mTextCouponCount);
                }
            } else {
                tvBalanceTokoPoint.setText(R.string.home_header_tokopoint_no_tokopoints);
                mTextCouponCount.setText(R.string.home_header_tokopoint_no_coupons);
                tvBalanceTokoPoint.setTextColor(getContext().getResources().getColor(R.color.font_black_primary_70));
                mTextCouponCount.setTextColor(getContext().getResources().getColor(R.color.tkpd_main_green));
            }

            tokoPointHolder.setOnClickListener(view -> {
                if (headerViewModel.getTokopointsDrawerHomeData() != null) {
                    HomePageTracking.eventUserProfileTokopoints(getContext());
                    listener.actionTokoPointClicked(
                            headerViewModel.getTokopointsDrawerHomeData().getRedirectURL(),
                            TextUtils.isEmpty(headerViewModel.getTokopointsDrawerHomeData().getMainPageTitle())
                                    ? TITLE_HEADER_WEBSITE
                                    : headerViewModel.getTokopointsDrawerHomeData().getMainPageTitle()
                    );
                }
            });
        }

    }

    private void setTokopointsHeaderData(SectionContentItem sectionContentItem, TextView tokopointsTextView) {
        if (sectionContentItem != null) {
            if (sectionContentItem.getTagAttributes() != null && !TextUtils.isEmpty(sectionContentItem.getTagAttributes().getText())) {
                if (!TextUtils.isEmpty(sectionContentItem.getTagAttributes().getBackgroundColour()) && HexValidator.validate(sectionContentItem.getTagAttributes().getBackgroundColour())) {
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_tokopoints_rounded);
                    if (drawable instanceof GradientDrawable) {
                        GradientDrawable shapeDrawable = (GradientDrawable) drawable;
                        shapeDrawable.setColorFilter(Color.parseColor(sectionContentItem.getTagAttributes().getBackgroundColour()), PorterDuff.Mode.SRC_ATOP);
                        tokopointsTextView.setBackground(shapeDrawable);
                        int horizontalPadding = getContext().getResources().getDimensionPixelSize(R.dimen.dp_3);
                        int verticalPadding = getContext().getResources().getDimensionPixelSize(R.dimen.dp_2);
                        tokopointsTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.sp_8));
                        tokopointsTextView.setTypeface(null, Typeface.NORMAL);
                        tokopointsTextView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
                    }
                    tokopointsTextView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tokopointsTextView.setTextColor(getContext().getResources().getColor(R.color.font_black_primary_70));
                }
                if (!TextUtils.isEmpty(sectionContentItem.getTagAttributes().getText())) {
                    tokopointsTextView.setText(sectionContentItem.getTagAttributes().getText());
                }
            } else if (sectionContentItem.getTextAttributes() != null && !TextUtils.isEmpty(sectionContentItem.getTextAttributes().getText())) {
                if (!TextUtils.isEmpty(sectionContentItem.getTextAttributes().getColour()) && HexValidator.validate(sectionContentItem.getTextAttributes().getColour())) {
                    tokopointsTextView.setTextColor(Color.parseColor(sectionContentItem.getTextAttributes().getColour()));
                } else {
                    tokopointsTextView.setTextColor(getContext().getResources().getColor(R.color.font_black_primary_70));
                }
                if (sectionContentItem.getTextAttributes().isIsBold()) {
                    tokopointsTextView.setTypeface(null, Typeface.BOLD);
                } else {
                    tokopointsTextView.setTypeface(null, Typeface.NORMAL);
                }
                if (!TextUtils.isEmpty(sectionContentItem.getTextAttributes().getText())) {
                    tokopointsTextView.setText(sectionContentItem.getTextAttributes().getText());
                }

            }
        }
    }

    private void successRenderTokoCash(HomeHeaderWalletAction homeHeaderWalletAction) {
        tokocashProgressBar.setVisibility(GONE);
        tvTitleTokocash.setText(homeHeaderWalletAction.getLabelTitle());
        tvActionTokocash.setText(homeHeaderWalletAction.getLabelActionButton());
        tvActionTokocash.setOnClickListener(getOnClickTokocashActionButton(homeHeaderWalletAction));
        tokoCashHolder.setOnClickListener(getOnClickTokocashBalance(homeHeaderWalletAction));
        ivLogoTokocash.setImageResource(R.drawable.ic_tokocash);

        if (homeHeaderWalletAction.isLinked()) {
            tvBalanceTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setText(homeHeaderWalletAction.getBalance());

            tvActionTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? VISIBLE : GONE);
            tvTitleTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? GONE : VISIBLE);
        } else {
            tvBalanceTokocash.setVisibility(GONE);
            tvActionTokocash.setVisibility(VISIBLE);
            if (headerViewModel.isPendingTokocashChecked()
                    && headerViewModel.getCashBackData() != null) {
                if (headerViewModel.getCashBackData().getAmount() > 0) {
                    tvActionTokocash.setVisibility(GONE);
                    tvBalanceTokocash.setVisibility(VISIBLE);
                    tvBalanceTokocash.setText(headerViewModel.getCashBackData().getAmountText());
                    tvBalanceTokocash.setOnClickListener(
                            getOnClickPendingCashBackListener(homeHeaderWalletAction)
                    );
                }
            } else {
                listener.onRequestPendingCashBack();
            }
        }
    }

    private void successRenderOvo(HomeHeaderWalletAction homeHeaderWalletAction) {
        tokocashProgressBar.setVisibility(GONE);
        tvActionTokocash.setText(homeHeaderWalletAction.getLabelActionButton());
        tvActionTokocash.setOnClickListener(getOnclickOvoApplink(homeHeaderWalletAction.isLinked(), homeHeaderWalletAction.getAppLinkActionButton()));
        tokoCashHolder.setOnClickListener(getOnclickOvoApplink(homeHeaderWalletAction.isLinked(), homeHeaderWalletAction.getAppLinkBalance()));

        if (homeHeaderWalletAction.isLinked()) {
            tvTitleTokocash.setText(homeHeaderWalletAction.getCashBalance());
            tvBalanceTokocash.setText(getResources().getString(R.string.home_header_fintech_points, homeHeaderWalletAction.getPointBalance()));

            tvActionTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? VISIBLE : GONE);
            tvTitleTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? GONE : VISIBLE);
        } else {
            tvTitleTokocash.setText(TITLE);
            tvActionTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setVisibility(GONE);
            if (headerViewModel.isPendingTokocashChecked() && headerViewModel.getCashBackData() != null) {
                if (headerViewModel.getCashBackData().getAmount() > 0) {
                    tvTitleTokocash.setText("(+ " + headerViewModel.getCashBackData().getAmountText() + ")");
                }
            } else {
                listener.onRequestPendingCashBack();
            }
        }
    }

    private OnClickListener getOnClickRefreshTokoPoint() {
        return view -> {
            tokopointProgressBarLayout.setVisibility(VISIBLE);
            listener.onRefreshTokoPointButtonClicked();
        };
    }

    private OnClickListener getOnClickRefreshTokocash() {
        return view -> {
            tokocashProgressBar.setVisibility(VISIBLE);
            listener.onRefreshTokoCashButtonClicked();
        };
    }

    @NonNull
    private OnClickListener getOnClickPendingCashBackListener(final HomeHeaderWalletAction homeHeaderWalletAction) {
        return view -> listener.actionInfoPendingCashBackTokocash(
                headerViewModel.getCashBackData(),
                homeHeaderWalletAction.getAppLinkActionButton()
        );
    }

    @NonNull
    private OnClickListener getOnClickTokocashActionButton(final HomeHeaderWalletAction homeHeaderWalletAction) {
        return v -> {
            if (!homeHeaderWalletAction.getAppLinkActionButton().contains("webview") &&
                    !homeHeaderWalletAction.isLinked()) {
                HomePageTracking.eventTokoCashActivateClick(getContext());
            }

            listener.actionAppLinkWalletHeader(homeHeaderWalletAction.getAppLinkActionButton());
        };
    }

    private OnClickListener getOnclickOvoApplink(boolean linkedOvo, String applinkString) {
        return view -> {
            if (RouteManager.isSupportApplink(getContext(), applinkString)) {
                Intent intentBalanceWalet = RouteManager.getIntent(getContext(), applinkString);
                getContext().startActivity(intentBalanceWalet);
                if (!linkedOvo) {
                    showAnimationBottomSheetActivation();
                    walletAnalytics.eventClickActivationOvoHomepage();
                }
            }
        };
    }

    private void showAnimationBottomSheetActivation() {
        if (!(getContext() instanceof Activity) && (getContext() instanceof ContextWrapper)) {
            Context context = ((ContextWrapper) getContext()).getBaseContext();
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.anim_slide_up_in, R.anim.anim_page_stay);
        }
    }

    @NonNull
    private OnClickListener getOnClickTokocashBalance(final HomeHeaderWalletAction homeHeaderWalletAction) {
        return v -> {
            if (!homeHeaderWalletAction.getAppLinkBalance().equals("") &&
                    !homeHeaderWalletAction.getAppLinkBalance().contains("webview") &&
                    homeHeaderWalletAction.isLinked()) {
                HomePageTracking.eventTokoCashCheckSaldoClick(getContext());
            }

            listener.actionAppLinkWalletHeader(homeHeaderWalletAction.getAppLinkBalance());
        };
    }
}
