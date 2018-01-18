package com.tokopedia.tkpd.beranda.presentation.view.compoundview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;

/**
 * @author anggaprasetiyo on 11/12/17.
 */

public class HeaderHomeView extends BaseCustomView {
    private static final String TITLE_HEADER_WEBSITE = "TokoPoints";
    private HomeCategoryListener listener;
    private HeaderViewModel headerViewModel;

    private View tokoCashHolder;
    private TextView tvTitleTokocash;
    private TextView tvBalanceTokocash;
    private ImageView ivLogoTokocash;
    private TextView tvActionTokocash;

    private View tokoPointHolder;
    private TextView scannerQR;

    private TextView tvTitleTokoPoint;
    private TextView tvBalanceTokoPoint;
    private ImageView ivLogoTokoPoint;

    public HeaderHomeView(@NonNull Context context, HeaderViewModel headerViewModel, HomeCategoryListener listener) {
        super(context);
        this.headerViewModel = headerViewModel;
        this.listener = listener;
        switch (headerViewModel.getType()) {
            case HeaderViewModel.TYPE_TOKOCASH_ONLY:
                renderHeaderOnlyTokocash();
                break;
            case HeaderViewModel.TYPE_TOKOPINT_ONLY:
                renderHeaderTokoPointOnly();
                break;
            case HeaderViewModel.TYPE_TOKOCASH_WITH_TOKOPOINT:
                renderHeaderTokocashWithTokopoint();
                break;
            default:
                setVisibility(GONE);
                break;
        }
    }

    private void renderHeaderTokoPointOnly() {
        View view = inflate(getContext(), R.layout.layout_item_header_home_no_tokocash, this);
        tokoPointHolder = view.findViewById(R.id.container_tokopoint);
        tvTitleTokoPoint = view.findViewById(R.id.tv_title_tokopoint);
        tvBalanceTokoPoint = view.findViewById(R.id.tv_balance_tokopoint);
        ivLogoTokoPoint = view.findViewById(R.id.iv_logo_tokopoint);

        if (headerViewModel.getTokoPointDrawerData() != null) renderTokoPointLayoutListener();

    }

    private void renderHeaderTokocashWithTokopoint() {
        View view = inflate(getContext(), R.layout.layout_item_header_home_with_tokopoint, this);
        tokoCashHolder = view.findViewById(R.id.container_tokocash);
        tvActionTokocash = view.findViewById(R.id.tv_btn_action_tokocash);
        tvTitleTokocash = view.findViewById(R.id.tv_title_tokocash);
        tvBalanceTokocash = view.findViewById(R.id.tv_balance_tokocash);
        ivLogoTokocash = view.findViewById(R.id.iv_logo_tokocash);
        scannerQR = view.findViewById(R.id.scanner_qr_with_toko_point);

        tokoPointHolder = view.findViewById(R.id.container_tokopoint);
        tvTitleTokoPoint = view.findViewById(R.id.tv_title_tokopoint);
        tvBalanceTokoPoint = view.findViewById(R.id.tv_balance_tokopoint);
        ivLogoTokoPoint = view.findViewById(R.id.iv_logo_tokopoint);

        if (headerViewModel.getHomeHeaderWalletActionData() != null) renderTokocashLayoutListener();
        renderTokoPointLayoutListener();
    }

    @SuppressLint("SetTextI18n")
    private void renderTokoPointLayoutListener() {
        tvTitleTokoPoint.setVisibility(VISIBLE);
        tvBalanceTokoPoint.setVisibility(VISIBLE);
        tvTitleTokoPoint.setText(headerViewModel.getTokoPointDrawerData().getUserTier().getTierNameDesc());
        tvBalanceTokoPoint.setText(headerViewModel.getTokoPointDrawerData().getUserTier().getRewardPointsStr());
        tokoPointHolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventUserProfileTokopoints();
                listener.actionTokoPointClicked(
                        headerViewModel.getTokoPointDrawerData().getMainPageUrl(),
                        TextUtils.isEmpty(headerViewModel.getTokoPointDrawerData().getMainPageTitle())
                                ? TITLE_HEADER_WEBSITE
                                : headerViewModel.getTokoPointDrawerData().getMainPageTitle()
                );
            }
        });
    }

    private void renderVisibilityTitleOnlyTokoCash(boolean isVisibleButtonAction) {
        if (headerViewModel.getHomeHeaderWalletActionData().getTypeAction() ==
                HomeHeaderWalletAction.TYPE_ACTION_ACTIVATION) {
            if (headerViewModel.getCashBackData() != null &&
                    headerViewModel.getCashBackData().getAmount() == 0) {
                tvTitleTokocash.setVisibility(VISIBLE);
            } else {
                tvTitleTokocash.setVisibility(GONE);
            }
        } else {
            if (!isVisibleButtonAction && scannerQR.getVisibility() == GONE) {
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
            }
        }
    }

    private void renderTokocashLayoutListener() {
        final HomeHeaderWalletAction homeHeaderWalletAction =
                headerViewModel.getHomeHeaderWalletActionData();

        tvTitleTokocash.setText(homeHeaderWalletAction.getLabelTitle());
        tvActionTokocash.setText(homeHeaderWalletAction.getLabelActionButton());

        if (homeHeaderWalletAction.getTypeAction()
                == HomeHeaderWalletAction.TYPE_ACTION_TOP_UP) {
            tokoCashHolder.setOnClickListener(getOnClickTokocashBalance(homeHeaderWalletAction));
            tvBalanceTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvBalanceTokocash.setVisibility(VISIBLE);
            tvBalanceTokocash.setText(homeHeaderWalletAction.getBalance());
            tvBalanceTokocash.setTextColor(getContext().getResources().getColor(R.color.black_70));
            tvBalanceTokocash.setTypeface(null, Typeface.BOLD);

            tvActionTokocash.setText(getContext().getString(R.string.top_up_button));
            tvActionTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? VISIBLE : GONE);
            tvTitleTokocash.setVisibility(homeHeaderWalletAction.isVisibleActionButton() ? GONE : VISIBLE);

            if (homeHeaderWalletAction.getAbTags().size() > 0) {
                for (int i = 0; i < homeHeaderWalletAction.getAbTags().size(); i++) {
                    if (homeHeaderWalletAction.getAbTags().get(i).equals("QR")) {
                        scannerQR.setVisibility(VISIBLE);
                    }
                }
            }
            if (scannerQR.getId() == R.id.scanner_qr) {
                tvActionTokocash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_topup,
                        0, 0, 0);
                tvActionTokocash.setTypeface(null, Typeface.BOLD);
            }
            scannerQR.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_qr_scanner,
                    0, 0, 0);
        } else {
            scannerQR.setVisibility(GONE);
            tvTitleTokocash.setTypeface(null, Typeface.NORMAL);
            tvActionTokocash.setVisibility(VISIBLE);
            tvActionTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tokoCashHolder.setOnClickListener(getOnClickTokocashActionButton(homeHeaderWalletAction));
            if (headerViewModel.isPendingTokocashChecked()
                    && headerViewModel.getCashBackData() != null) {
                if (headerViewModel.getCashBackData().getAmount() > 0) {
                    tvBalanceTokocash.setVisibility(VISIBLE);
                    tvBalanceTokocash.setText(headerViewModel.getCashBackData().getAmountText());
                    tvBalanceTokocash.setTextColor(
                            getContext().getResources().getColor(R.color.black_38)
                    );
                    tvBalanceTokocash.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info, 0);
                    tvBalanceTokocash.setOnClickListener(
                            getOnClickPendingCashBackListener(homeHeaderWalletAction)
                    );
                }
            } else {
                listener.onRequestPendingCashBack();
            }
        }

        scannerQR.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.actionScannerQRTokoCash();
            }
        });
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
                        homeHeaderWalletAction.getTypeAction() == HomeHeaderWalletAction.TYPE_ACTION_ACTIVATION) {
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
                        homeHeaderWalletAction.getTypeAction() == HomeHeaderWalletAction.TYPE_ACTION_TOP_UP) {
                    UnifyTracking.eventTokoCashCheckSaldoClick();

                    listener.actionAppLinkWalletHeader(
                            homeHeaderWalletAction.getRedirectUrlBalance(),
                            homeHeaderWalletAction.getAppLinkBalance()
                    );
                }
            }
        };
    }

    private void renderHeaderOnlyTokocash() {
        View view = inflate(getContext(), R.layout.layout_item_header_home_no_tokopoint, this);
        tokoCashHolder = view.findViewById(R.id.container_tokocash);
        tvActionTokocash = view.findViewById(R.id.tv_btn_action_tokocash);
        tvTitleTokocash = view.findViewById(R.id.tv_title_tokocash);
        ivLogoTokocash = view.findViewById(R.id.iv_logo_tokocash);
        scannerQR = view.findViewById(R.id.scanner_qr);
        tvBalanceTokocash = view.findViewById(R.id.tv_balance_tokocash);

        if (headerViewModel.getHomeHeaderWalletActionData() != null) {
            renderTokocashLayoutListener();
            renderVisibilityTitleOnlyTokoCash(headerViewModel.getHomeHeaderWalletActionData()
                    .isVisibleActionButton());
        }
    }

    public HeaderHomeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderHomeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
