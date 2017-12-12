package com.tokopedia.tkpd.beranda.presentation.view.compoundview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
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
    private HomeCategoryListener listener;
    private HeaderViewModel headerViewModel;

    private TextView tvTitleTokocash;
    private TextView tvBalanceTokocash;
    private ImageView ivLogoTokocash;
    private TextView tvActionTokocash;


    private TextView tvTitleTokoPoint;
    private TextView tvBalanceTokoPoint;
    private ImageView ivLogoTokoPoint;

    private HomeHeaderWalletAction homeHeaderWalletAction;


    public HeaderHomeView(@NonNull Context context, HeaderViewModel headerViewModel, HomeCategoryListener listener) {
        super(context);
        this.headerViewModel = headerViewModel;
        this.listener = listener;
        switch (headerViewModel.getType()) {
            case HeaderViewModel.TYPE_TOKOCASH_ONLY:
                renderHeaderOnlyTokocash();
                break;
            default:
                renderHeaderTokocashWithTokopoint();
                break;
        }
    }

    private void renderHeaderTokocashWithTokopoint() {
        View view = inflate(getContext(), R.layout.layout_item_header_home_with_tokopoint, this);
        tvActionTokocash = view.findViewById(R.id.tv_btn_action_tokocash);
        tvTitleTokocash = view.findViewById(R.id.tv_title_tokocash);
        tvBalanceTokocash = view.findViewById(R.id.tv_balance_tokocash);
        ivLogoTokocash = view.findViewById(R.id.iv_logo_tokocash);

        tvTitleTokoPoint = view.findViewById(R.id.tv_title_tokopoint);
        tvBalanceTokoPoint = view.findViewById(R.id.tv_balance_tokopoint);
        ivLogoTokoPoint = view.findViewById(R.id.iv_logo_tokopoint);

        if (homeHeaderWalletAction != null) renderTokocashLayoutListener();
        renderTokoPointLayoutListener();
    }

    @SuppressLint("SetTextI18n")
    private void renderTokoPointLayoutListener() {
        tvTitleTokoPoint.setVisibility(VISIBLE);
        tvBalanceTokoPoint.setVisibility(VISIBLE);
        tvTitleTokoPoint.setText(headerViewModel.getTokoPointDrawerData().getUserTier().getTierName());
        tvBalanceTokoPoint.setText(headerViewModel.getTokoPointDrawerData().getUserTier().getRewardPoints() + " Poin");
    }

    private void renderTokocashLayoutListener() {

        homeHeaderWalletAction = headerViewModel.getHomeHeaderWalletActionData();
        tvTitleTokocash.setVisibility(VISIBLE);
        tvTitleTokocash.setText(homeHeaderWalletAction.getLabelTitle());
        tvBalanceTokocash.setText(homeHeaderWalletAction.getBalance());
        tvActionTokocash.setText(homeHeaderWalletAction.getLabelActionButton());
        tvActionTokocash.setVisibility(VISIBLE);
        if (homeHeaderWalletAction.getTypeAction()
                == HomeHeaderWalletAction.TYPE_ACTION_TOP_UP) {
            tvBalanceTokocash.setVisibility(VISIBLE);
            if (homeHeaderWalletAction.isVisibleActionButton())
                tvActionTokocash.setVisibility(VISIBLE);
            else tvActionTokocash.setVisibility(GONE);
            tvActionTokocash.setOnClickListener(new OnClickListener() {
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
            });
        } else {
            listener.onRequestPendingCashBack();
        }

        tvActionTokocash.setOnClickListener(new OnClickListener() {
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
        });
    }

    private void renderHeaderOnlyTokocash() {
        View view = inflate(getContext(), R.layout.layout_item_header_home_no_tokopoint, this);
        tvActionTokocash = view.findViewById(R.id.tv_btn_action_tokocash);
        tvTitleTokocash = view.findViewById(R.id.tv_title_tokocash);
        tvBalanceTokocash = view.findViewById(R.id.tv_balance_tokocash);
        ivLogoTokocash = view.findViewById(R.id.iv_logo_tokocash);

        renderTokocashLayoutListener();
    }

    public HeaderHomeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderHomeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
