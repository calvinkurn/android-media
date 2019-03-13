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
import android.graphics.drawable.ShapeDrawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
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
    private static final String TITLE_HEADER_WEBSITE = "TokoPoints";
    private static final String RP_NOL = "Rp0";
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
//    private LinearLayout tokocashActionContainer;
//    private ImageView imageInfoBtn;
//    private TextView pointsOvo;

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

        renderHeaderTokocashWithTokopoint();
    }

    public void setHeaderViewModel(HeaderViewModel headerViewModel) {
        this.headerViewModel = headerViewModel;
    }

    public void setListener(HomeCategoryListener listener) {
        this.listener = listener;
    }

    private void renderHeaderTokocashWithTokopoint() {
        if (view == null) {
            view = inflate(getContext(), R.layout.layout_header_home_with_tokopoint, this);
            scanHolder = view.findViewById(R.id.container_action_scan);
            tokoCashHolder = view.findViewById(R.id.container_tokocash);
            tvActionTokocash = view.findViewById(R.id.tv_btn_action_tokocash);
            tvTitleTokocash = view.findViewById(R.id.tv_title_tokocash);
            tvBalanceTokocash = view.findViewById(R.id.tv_balance_tokocash);
            ivLogoTokocash = view.findViewById(R.id.iv_logo_tokocash);
            tokocashProgressBar = view.findViewById(R.id.progress_bar_tokocash);
//            tokocashActionContainer = view.findViewById(R.id.container_action_tokocash);
//            imageInfoBtn = view.findViewById(R.id.info_button);
//            pointsOvo = view.findViewById(R.id.tv_ovo_point);

            tokoPointHolder = view.findViewById(R.id.container_tokopoint);
            tvBalanceTokoPoint = view.findViewById(R.id.tv_balance_tokopoint);
            tvActionTokopoint = view.findViewById(R.id.tv_btn_action_tokopoint);
            ivLogoTokoPoint = view.findViewById(R.id.iv_logo_tokopoint);
            tokopointProgressBarLayout = view.findViewById(R.id.progress_bar_tokopoint_layout);
            tokopointActionContainer = view.findViewById(R.id.container_action_tokopoint);
            mTextCouponCount = view.findViewById(R.id.text_coupon_count);
        }
        viewListener();
    }

    @SuppressLint("SetTextI18n")
    private void renderTokoPointLayoutListener() {
        if (headerViewModel.getTokopointsDrawerHomeData() == null && headerViewModel.isTokoPointDataError()) {
            tokoPointHolder.setOnClickListener(getOnClickRefreshTokoPoint());
            mTextCouponCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            mTextCouponCount.setVisibility(VISIBLE);
            mTextCouponCount.setText(R.string.home_header_tokopoint_unable_to_load_label);
            mTextCouponCount.setTextColor(getContext().getResources().getColor(R.color.black_70));
            tvActionTokopoint.setText(R.string.home_header_tokopoint_refresh_label);
            tvActionTokopoint.setVisibility(VISIBLE);
            tokopointProgressBarLayout.setVisibility(GONE);
            tokopointActionContainer.setVisibility(VISIBLE);
            ivLogoTokoPoint.setImageResource(R.drawable.ic_hachiko_egg);
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

            tokoPointHolder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (headerViewModel.getTokopointsDrawerHomeData() != null) {
                        HomePageTracking.eventUserProfileTokopoints(getContext());
                        listener.actionTokoPointClicked(
                                headerViewModel.getTokopointsDrawerHomeData().getRedirectURL(),
                                TextUtils.isEmpty(headerViewModel.getTokopointsDrawerHomeData().getMainPageTitle())
                                        ? TITLE_HEADER_WEBSITE
                                        : headerViewModel.getTokopointsDrawerHomeData().getMainPageTitle()
                        );
                    }
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

    private void renderVisibilityTitleOnlyTokoCash(boolean isVisibleButtonAction) {
        if (!headerViewModel.getHomeHeaderWalletActionData().isLinked()) {
            if (headerViewModel.getCashBackData() != null &&
                    headerViewModel.getCashBackData().getAmount() == 0) {
                tvTitleTokocash.setVisibility(VISIBLE);
            } else {
                tvTitleTokocash.setVisibility(GONE);
            }
        } else {
            if (!isVisibleButtonAction) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                        tvBalanceTokocash.getLayoutParams();
                tvTitleTokocash.setVisibility(VISIBLE);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvBalanceTokocash.setLayoutParams(params);
            } else {
                tvTitleTokocash.setVisibility(GONE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                        tvBalanceTokocash.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                tvBalanceTokocash.setLayoutParams(params);
                tvActionTokocash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_topup,
                        0, 0, 0);
                tvActionTokocash.setTypeface(null, Typeface.BOLD);
            }
        }
    }

    private void viewListener() {
        scanHolder.setOnClickListener(v ->
                getContext().startActivity(((IHomeRouter) getContext().getApplicationContext())
                .gotoQrScannerPage(false)));

        renderTokocashLayoutListener();
        renderTokoPointLayoutListener();
    }

    private void renderTokocashLayoutListener() {
        HomeHeaderWalletAction homeHeaderWalletAction =
                headerViewModel.getHomeHeaderWalletActionData();
//        pointsOvo.setVisibility(GONE);
        if (headerViewModel.getHomeHeaderWalletActionData() == null && headerViewModel.isWalletDataError()) {
            tokoCashHolder.setOnClickListener(getOnClickRefreshTokocash());
            tvBalanceTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvBalanceTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setText(R.string.home_header_tokocash_unable_to_load_label);
            tvBalanceTokocash.setTextColor(getContext().getResources().getColor(R.color.black_70));
            tvActionTokocash.setText(R.string.home_header_tokocash_refresh_label);
            tvActionTokocash.setVisibility(VISIBLE);
            tvTitleTokocash.setVisibility(GONE);
            tokocashProgressBar.setVisibility(GONE) ;
//            tokocashActionContainer.setVisibility(VISIBLE);
        } else if (headerViewModel.getHomeHeaderWalletActionData() == null && !headerViewModel.isWalletDataError()) {
            tokoCashHolder.setOnClickListener(null);
            tokocashProgressBar.setVisibility(VISIBLE);
//            tokocashActionContainer.setVisibility(GONE);
        } else {
            if (!TextUtils.isEmpty(homeHeaderWalletAction.getWalletType()) && homeHeaderWalletAction.getWalletType().equals(WALLET_TYPE)) {
                successRenderOvo(homeHeaderWalletAction);
            } else {
                successRenderTokoCash(homeHeaderWalletAction);
            }

        }

    }

    private void successRenderTokoCash(HomeHeaderWalletAction homeHeaderWalletAction) {
        tokocashProgressBar.setVisibility(GONE);
//        tokocashActionContainer.setVisibility(VISIBLE);
        tvTitleTokocash.setText(homeHeaderWalletAction.getLabelTitle());
        tvActionTokocash.setText(homeHeaderWalletAction.getLabelActionButton());
        tvActionTokocash.setOnClickListener(getOnClickTokocashActionButton(homeHeaderWalletAction));
        tokoCashHolder.setOnClickListener(getOnClickTokocashBalance(homeHeaderWalletAction));
        ivLogoTokocash.setImageResource(R.drawable.ic_tokocash);
        tvTitleTokocash.setTextColor(getContext().getResources().getColor(R.color.font_black_disabled_38));

        if (homeHeaderWalletAction.isLinked()) {
            tvBalanceTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvBalanceTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setText(homeHeaderWalletAction.getBalance());
            tvBalanceTokocash.setTextColor(getContext().getResources().getColor(R.color.font_black_primary_70));

            tvActionTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? VISIBLE : GONE);
            tvTitleTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? GONE : VISIBLE);
//            imageInfoBtn.setVisibility(GONE);
        } else {
//            imageInfoBtn.setVisibility(GONE);
//            tvTitleTokocash.setTypeface(null, Typeface.NORMAL);
            tvBalanceTokocash.setVisibility(GONE);
            tvActionTokocash.setVisibility(VISIBLE);
            tvActionTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            if (headerViewModel.isPendingTokocashChecked()
                    && headerViewModel.getCashBackData() != null) {
                if (headerViewModel.getCashBackData().getAmount() > 0) {
                    tvActionTokocash.setVisibility(GONE);
                    tvBalanceTokocash.setVisibility(VISIBLE);
                    tvBalanceTokocash.setText(headerViewModel.getCashBackData().getAmountText());
                    tvBalanceTokocash.setTextColor(
                            getContext().getResources().getColor(R.color.font_black_disabled_38)
                    );
//                    imageInfoBtn.setVisibility(VISIBLE);
//                    imageInfoBtn.setOnClickListener(
//                            getOnClickPendingCashBackListener(homeHeaderWalletAction)
//                    );
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
//        tokocashActionContainer.setVisibility(VISIBLE);
        tvActionTokocash.setText(homeHeaderWalletAction.getLabelActionButton());
        tvActionTokocash.setOnClickListener(getOnclickOvoApplink(homeHeaderWalletAction.isLinked(), homeHeaderWalletAction.getAppLinkActionButton()));
        tokoCashHolder.setOnClickListener(getOnclickOvoApplink(homeHeaderWalletAction.isLinked(), homeHeaderWalletAction.getAppLinkBalance()));
        ivLogoTokocash.setImageResource(R.drawable.wallet_ic_ovo_home);
//        tvTitleTokocash.setTextColor(getContext().getResources().getColor(R.color.font_black_disabled_38));

        if (homeHeaderWalletAction.isLinked()) {
//            pointsOvo.setVisibility(VISIBLE);
            tvTitleTokocash.setText(homeHeaderWalletAction.getCashBalance());
            tvTitleTokocash.setTextColor(getContext().getResources().getColor(R.color.font_black_primary_70));
            tvBalanceTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvBalanceTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setText(getResources().getString(R.string.home_header_fintech_points, homeHeaderWalletAction.getPointBalance()));
            tvBalanceTokocash.setTextColor(getContext().getResources().getColor(R.color.font_black_primary_70));
            tvBalanceTokocash.setTypeface(null, Typeface.NORMAL);

            tvActionTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? VISIBLE : GONE);
            tvTitleTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? GONE : VISIBLE);
//            imageInfoBtn.setVisibility(GONE);
        } else {
            tvTitleTokocash.setText(RP_NOL);
//            tvTitleTokocash.setTypeface(null, Typeface.NORMAL);
            tvActionTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setVisibility(GONE);
//            imageInfoBtn.setVisibility(GONE);
            tvActionTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            if (headerViewModel.isPendingTokocashChecked()
                    && headerViewModel.getCashBackData() != null) {
                if (headerViewModel.getCashBackData().getAmount() > 0) {
                    tvTitleTokocash.setText("(+ " + headerViewModel.getCashBackData().getAmountText() + ")");
                }
            } else {
                listener.onRequestPendingCashBack();
            }
        }
    }


    private OnClickListener getOnClickRefreshTokoPoint() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRefreshTokoPointButtonClicked();
            }
        };
    }

    private OnClickListener getOnClickRefreshTokocash() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRefreshTokoCashButtonClicked();
            }
        };
    }

    @NonNull
    private OnClickListener getOnClickPendingCashBackListener(final HomeHeaderWalletAction homeHeaderWalletAction) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.actionInfoPendingCashBackTokocash(
                        headerViewModel.getCashBackData(),
                        homeHeaderWalletAction.getAppLinkActionButton()
                );
            }
        };
    }

    @NonNull
    private OnClickListener getOnClickTokocashActionButton(final HomeHeaderWalletAction homeHeaderWalletAction) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!homeHeaderWalletAction.getAppLinkActionButton().contains("webview") &&
                        !homeHeaderWalletAction.isLinked()) {
                    HomePageTracking.eventTokoCashActivateClick(getContext());
                }

                listener.actionAppLinkWalletHeader(homeHeaderWalletAction.getAppLinkActionButton());
            }
        };
    }

    private OnClickListener getOnclickOvoApplink(boolean linkedOvo, String applinkString) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RouteManager.isSupportApplink(getContext(), applinkString)) {
                    Intent intentBalanceWalet = RouteManager.getIntent(getContext(), applinkString);
                    getContext().startActivity(intentBalanceWalet);
                    if (!linkedOvo) {
                        showAnimationBottomSheetActivation();
                        walletAnalytics.eventClickActivationOvoHomepage();
                    }
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
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!homeHeaderWalletAction.getAppLinkBalance().equals("") &&
                        !homeHeaderWalletAction.getAppLinkBalance().contains("webview") &&
                        homeHeaderWalletAction.isLinked()) {
                    HomePageTracking.eventTokoCashCheckSaldoClick(getContext());
                }

                listener.actionAppLinkWalletHeader(homeHeaderWalletAction.getAppLinkBalance());
            }
        };
    }

    private void renderHeaderOnlyTokocash() {
        View view = inflate(getContext(), R.layout.layout_item_header_home_no_tokopoint, this);
        tokoCashHolder = view.findViewById(R.id.container_tokocash);
        tvActionTokocash = view.findViewById(R.id.tv_btn_action_tokocash);
        tvTitleTokocash = view.findViewById(R.id.tv_title_tokocash);
        ivLogoTokocash = view.findViewById(R.id.iv_logo_tokocash);
        tvBalanceTokocash = view.findViewById(R.id.tv_balance_tokocash);
        tokocashProgressBar = view.findViewById(R.id.progress_bar_tokocash);
//        tokocashActionContainer = view.findViewById(R.id.container_action_tokocash);
//        imageInfoBtn = view.findViewById(R.id.info_button);
        renderTokocashLayoutListener();
        if (headerViewModel.getHomeHeaderWalletActionData() != null) {
            renderVisibilityTitleOnlyTokoCash(headerViewModel.getHomeHeaderWalletActionData()
                    .isVisibleActionButton());
        }
    }
}
