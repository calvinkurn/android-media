package com.tokopedia.home.beranda.presentation.view.compoundview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;

/**
 * @author anggaprasetiyo on 11/12/17.
 */

public class HeaderHomeView extends BaseCustomView {
    private static final String TITLE_HEADER_WEBSITE = "TokoPoints";
    private HomeCategoryListener listener;
    private HeaderViewModel headerViewModel;

    private View view;

    private View tokoCashHolder;
    private TextView tvTitleTokocash;
    private TextView tvBalanceTokocash;
    private ImageView ivLogoTokocash;
    private TextView tvActionTokocash;
    private ProgressBar tokocashProgressBar;
    private LinearLayout tokocashActionContainer;
    private ImageView imageInfoBtn;

    private View tokoPointHolder;
    private TextView tvTitleTokoPoint;
    private TextView tvBalanceTokoPoint;
    private TextView tvActionTokopoint;
    private ImageView ivLogoTokoPoint;
    private LinearLayout tokopointProgressBarLayout;
    private LinearLayout tokopointActionContainer;

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

        if (headerViewModel.getTokoPointDrawerData() != null && headerViewModel.getTokoPointDrawerData().getOffFlag() == 1) {
            renderHeaderOnlyTokocash();
        } else {
            renderHeaderTokocashWithTokopoint();
        }
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
            tokoCashHolder = view.findViewById(R.id.container_tokocash);
            tvActionTokocash = view.findViewById(R.id.tv_btn_action_tokocash);
            tvTitleTokocash = view.findViewById(R.id.tv_title_tokocash);
            tvBalanceTokocash = view.findViewById(R.id.tv_balance_tokocash);
            ivLogoTokocash = view.findViewById(R.id.iv_logo_tokocash);
            tokocashProgressBar = view.findViewById(R.id.progress_bar_tokocash);
            tokocashActionContainer = view.findViewById(R.id.container_action_tokocash);
            imageInfoBtn = view.findViewById(R.id.info_button);

            tokoPointHolder = view.findViewById(R.id.container_tokopoint);
            tvTitleTokoPoint = view.findViewById(R.id.tv_title_tokopoint);
            tvBalanceTokoPoint = view.findViewById(R.id.tv_balance_tokopoint);
            tvActionTokopoint = view.findViewById(R.id.tv_btn_action_tokopoint);
            ivLogoTokoPoint = view.findViewById(R.id.iv_logo_tokopoint);
            tokopointProgressBarLayout = view.findViewById(R.id.progress_bar_tokopoint_layout);
            tokopointActionContainer = view.findViewById(R.id.container_action_tokopoint);
        }
        renderTokocashLayoutListener();
        renderTokoPointLayoutListener();
    }

    @SuppressLint("SetTextI18n")
    private void renderTokoPointLayoutListener() {
        if (headerViewModel.getTokoPointDrawerData() == null && headerViewModel.isTokoPointDataError()) {
            tokoPointHolder.setOnClickListener(getOnClickRefreshTokoPoint());
            tvBalanceTokoPoint.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvBalanceTokoPoint.setVisibility(VISIBLE);
            tvBalanceTokoPoint.setText(R.string.home_header_tokopoint_unable_to_load_label);
            tvBalanceTokoPoint.setTextColor(getContext().getResources().getColor(R.color.black_70));
            tvBalanceTokoPoint.setTypeface(null, Typeface.BOLD);
            tvActionTokopoint.setText(R.string.home_header_tokopoint_refresh_label);
            tvActionTokopoint.setVisibility(VISIBLE);
            tvTitleTokoPoint.setVisibility(GONE);
            tokopointProgressBarLayout.setVisibility(GONE);
            tokopointActionContainer.setVisibility(VISIBLE);
        } else if (headerViewModel.getTokoPointDrawerData() == null && !headerViewModel.isTokoPointDataError()) {
            tokoPointHolder.setOnClickListener(null);
            tokopointProgressBarLayout.setVisibility(VISIBLE);
            tokopointActionContainer.setVisibility(GONE);
        } else {
            tokopointProgressBarLayout.setVisibility(GONE);
            tokopointActionContainer.setVisibility(VISIBLE);
            tvActionTokopoint.setVisibility(GONE);
            tvTitleTokoPoint.setVisibility(VISIBLE);
            tvBalanceTokoPoint.setVisibility(VISIBLE);
            //tvTitleTokoPoint.setText(headerViewModel.getTokoPointDrawerData().getUserTier().getTierNameDesc());
            tvTitleTokoPoint.setText(TITLE_HEADER_WEBSITE);
            tvBalanceTokoPoint.setText(headerViewModel.getTokoPointDrawerData().getUserTier().getRewardPointsStr());
            tokoPointHolder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (headerViewModel.getTokoPointDrawerData() != null) {
                        UnifyTracking.eventUserProfileTokopoints();
                        listener.actionTokoPointClicked(
                                headerViewModel.getTokoPointDrawerData().getMainPageUrl(),
                                TextUtils.isEmpty(headerViewModel.getTokoPointDrawerData().getMainPageTitle())
                                        ? TITLE_HEADER_WEBSITE
                                        : headerViewModel.getTokoPointDrawerData().getMainPageTitle()
                        );
                    }
                }
            });
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

    private void renderTokocashLayoutListener() {
        HomeHeaderWalletAction homeHeaderWalletAction =
                headerViewModel.getHomeHeaderWalletActionData();
        if (headerViewModel.getHomeHeaderWalletActionData() == null && headerViewModel.isWalletDataError()) {
            tokoCashHolder.setOnClickListener(getOnClickRefreshTokocash());
            tvBalanceTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvBalanceTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setText(R.string.home_header_tokocash_unable_to_load_label);
            tvBalanceTokocash.setTextColor(getContext().getResources().getColor(R.color.black_70));
            tvBalanceTokocash.setTypeface(null, Typeface.BOLD);
            tvActionTokocash.setText(R.string.home_header_tokocash_refresh_label);
            tvActionTokocash.setVisibility(VISIBLE);
            tvTitleTokocash.setVisibility(GONE);
            tokocashProgressBar.setVisibility(GONE);
            tokocashActionContainer.setVisibility(VISIBLE);
        } else if (headerViewModel.getHomeHeaderWalletActionData() == null && !headerViewModel.isWalletDataError()) {
            tokoCashHolder.setOnClickListener(null);
            tokocashProgressBar.setVisibility(VISIBLE);
            tokocashActionContainer.setVisibility(GONE);
        } else {
            tokocashProgressBar.setVisibility(GONE);
            tokocashActionContainer.setVisibility(VISIBLE);
            tvTitleTokocash.setText(homeHeaderWalletAction.getLabelTitle());
            tvActionTokocash.setText(homeHeaderWalletAction.getLabelActionButton());
            tokoCashHolder.setOnClickListener(getOnClickTokocashBalance(homeHeaderWalletAction));

            if (homeHeaderWalletAction.isLinked()) {
                tvBalanceTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvBalanceTokocash.setVisibility(VISIBLE);
                tvBalanceTokocash.setText(homeHeaderWalletAction.getBalance());
                tvBalanceTokocash.setTextColor(getContext().getResources().getColor(R.color.black_70));
                tvBalanceTokocash.setTypeface(null, Typeface.BOLD);

                tvActionTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? VISIBLE : GONE);
                tvTitleTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? GONE : VISIBLE);
                imageInfoBtn.setVisibility(GONE);
            } else {
                imageInfoBtn.setVisibility(GONE);
                tvTitleTokocash.setTypeface(null, Typeface.NORMAL);
                tvBalanceTokocash.setVisibility(GONE);
                tvActionTokocash.setVisibility(VISIBLE);
                tvActionTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (headerViewModel.isPendingTokocashChecked()
                        && headerViewModel.getCashBackData() != null) {
                    if (headerViewModel.getCashBackData().getAmount() > 0) {
                        tvBalanceTokocash.setVisibility(VISIBLE);
                        tvBalanceTokocash.setText(headerViewModel.getCashBackData().getAmountText());
                        tvBalanceTokocash.setTextColor(
                                getContext().getResources().getColor(R.color.black_38)
                        );
                        imageInfoBtn.setVisibility(VISIBLE);
                        imageInfoBtn.setOnClickListener(
                                getOnClickPendingCashBackListener(homeHeaderWalletAction)
                        );
                        tvBalanceTokocash.setOnClickListener(
                                getOnClickPendingCashBackListener(homeHeaderWalletAction)
                        );
                    }
                } else {
                    listener.onRequestPendingCashBack();
                }
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
                        homeHeaderWalletAction.getRedirectUrlActionButton(),
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
                    UnifyTracking.eventTokoCashActivateClick();
                }

                listener.actionAppLinkWalletHeader(
                        homeHeaderWalletAction.getRedirectUrlActionButton(),
                        homeHeaderWalletAction.getAppLinkActionButton()
                );
            }
        };
    }

    @NonNull
    private OnClickListener getOnClickTokocashBalance(final HomeHeaderWalletAction homeHeaderWalletAction) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!homeHeaderWalletAction.getAppLinkBalance().equals("") &&
                        !homeHeaderWalletAction.getAppLinkBalance().contains("webview") &&
                        homeHeaderWalletAction.isLinked()) {
                    UnifyTracking.eventTokoCashCheckSaldoClick();
                }

                listener.actionAppLinkWalletHeader(
                        homeHeaderWalletAction.getRedirectUrlBalance(),
                        homeHeaderWalletAction.getAppLinkBalance()
                );
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
        tokocashActionContainer = view.findViewById(R.id.container_action_tokocash);
        imageInfoBtn = view.findViewById(R.id.info_button);
        renderTokocashLayoutListener();
        if (headerViewModel.getHomeHeaderWalletActionData() != null) {
            renderVisibilityTitleOnlyTokoCash(headerViewModel.getHomeHeaderWalletActionData()
                    .isVisibleActionButton());
        }
    }
}
