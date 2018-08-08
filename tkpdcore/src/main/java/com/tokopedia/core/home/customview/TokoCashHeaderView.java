package com.tokopedia.core.home.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;

/**
 * Created by kris on 4/21/17. Tokopedia
 */

public class TokoCashHeaderView extends RelativeLayout {

    private TextView headerTokoCashLabel;
    private TextView tokoCashAmount;
    private TextView tokoCashButton;
    private ActionListener actionListener;
    private LinearLayout pendingLayout;
    private LinearLayout normalLayout;
    private TextView pendingAmount;
    private TextView pendingCashBackInfo;
    private HomeHeaderWalletAction homeHeaderWalletAction;

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public TokoCashHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public TokoCashHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TokoCashHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void renderData(final DrawerTokoCash tokoCashData,
                           boolean showTopUpButton,
                           String tokoCashLabel) {
        homeHeaderWalletAction = tokoCashData.getHomeHeaderWalletAction();
        headerTokoCashLabel.setText(homeHeaderWalletAction.getLabelTitle());
        tokoCashAmount.setText(homeHeaderWalletAction.getBalance());
        tokoCashButton.setText(homeHeaderWalletAction.getLabelActionButton());
        tokoCashButton.setVisibility(VISIBLE);
        if (homeHeaderWalletAction.getTypeAction()
                == HomeHeaderWalletAction.TYPE_ACTION_TOP_UP) {
            pendingLayout.setVisibility(GONE);
            normalLayout.setVisibility(VISIBLE);
            if (homeHeaderWalletAction.isVisibleActionButton())
                tokoCashButton.setVisibility(VISIBLE);
            else tokoCashButton.setVisibility(GONE);
            normalLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!homeHeaderWalletAction.getAppLinkBalance().equals("") &&
                            !homeHeaderWalletAction.getAppLinkBalance().contains("webview") &&
                            homeHeaderWalletAction.getTypeAction() == HomeHeaderWalletAction.TYPE_ACTION_TOP_UP) {
                        UnifyTracking.eventTokoCashCheckSaldoClick();

                        actionListener.actionAppLinkWalletHeader(
                                homeHeaderWalletAction.getRedirectUrlBalance(),
                                homeHeaderWalletAction.getAppLinkBalance()
                        );
                    }
                }
            });
        } else {
            actionListener.onRequestPendingCashBack();
        }

        tokoCashButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!homeHeaderWalletAction.getAppLinkActionButton().contains("webview") &&
                        homeHeaderWalletAction.getTypeAction() == HomeHeaderWalletAction.TYPE_ACTION_ACTIVATION) {
                    UnifyTracking.eventTokoCashActivateClick();
                }
                actionListener.actionAppLinkWalletHeader(
                        homeHeaderWalletAction.getRedirectUrlActionButton(),
                        homeHeaderWalletAction.getAppLinkActionButton()
                );
            }
        });
    }

    public void showPendingTokoCash(String amount) {
        normalLayout.setVisibility(GONE);
        pendingLayout.setVisibility(VISIBLE);
        pendingAmount.setText(amount);
        pendingCashBackInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onShowTokoCashBottomSheet();
            }
        });
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.toko_cash_header_view_home, this, true);

        headerTokoCashLabel = (TextView) findViewById(R.id.header_toko_cash_label);
        pendingLayout = (LinearLayout) findViewById(R.id.pending_cash_back_layout);
        normalLayout = (LinearLayout) findViewById(R.id.toko_cash_normal_layout);
        tokoCashAmount = (TextView) findViewById(R.id.header_toko_cash_amount);
        tokoCashButton = (TextView) findViewById(R.id.header_toko_cash_button);
        pendingAmount = (TextView) findViewById(R.id.pending_amount);
        pendingCashBackInfo = (TextView) findViewById(R.id.pending_cashback_info);
    }

    public interface ActionListener {

        void onRequestPendingCashBack();

        void onShowTokoCashBottomSheet();

        void actionAppLinkWalletHeader(String redirectUrl, String appLinkScheme);
    }
}