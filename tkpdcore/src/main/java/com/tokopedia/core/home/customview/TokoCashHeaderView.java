package com.tokopedia.core.home.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;

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
        headerTokoCashLabel.setText(tokoCashLabel);
        if (tokoCashData.getLink() == 1) {
            setOnClickListener(onMainViewClickedListener());
            tokoCashAmount.setText(tokoCashData.getBalance());
            tokoCashButton.setText(getContext().getString(R.string.toko_cash_top_up));
            tokoCashButton.setOnClickListener(getTopUpClickedListener(tokoCashLabel));
            pendingLayout.setVisibility(GONE);
            normalLayout.setVisibility(VISIBLE);
            if (showTopUpButton) tokoCashButton.setVisibility(VISIBLE);
            else tokoCashButton.setVisibility(GONE);
        } else {
            actionListener.onRequestPendingCashBack();
            tokoCashButton.setOnClickListener(onActivationClickedListener());
            tokoCashButton.setText(getContext().getString(R.string.toko_cash_activation));
        }
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

    @NonNull
    private OnClickListener getTopUpClickedListener(final String tokocashLabel) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onTopUpTokoCashClicked(tokocashLabel);
            }
        };
    }

    private OnClickListener onActivationClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onActivationTokoCashClicked();
            }
        };
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

    private OnClickListener onMainViewClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onWalletHistoryClicked();
            }
        };
    }

    public interface ActionListener {
        void onWalletHistoryClicked();

        void onTopUpTokoCashClicked(String tokocashLabel);

        void onActivationTokoCashClicked();

        void onRequestPendingCashBack();

        void onShowTokoCashBottomSheet();
    }
}