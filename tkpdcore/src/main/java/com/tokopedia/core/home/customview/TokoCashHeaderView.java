package com.tokopedia.core.home.customview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;

/**
 * Created by kris on 4/21/17. Tokopedia
 */

public class TokoCashHeaderView extends RelativeLayout {

    private static final String TOKO_CASH_URL = "url";
    private TextView headerTokoCashLabel;
    private TextView tokoCashAmount;
    private TextView tokoCashButton;
    private ActionListener actionListener;
    private LinearLayout pendingLayout;
    private LinearLayout normalLayout;
    private TextView pendingAmount;
    private TextView pendingCashBackInfo;

    private String activationRedirectUrl;

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
        String tokoCashRedirectUrl = tokoCashData.getRedirectUrl();
        final String tokoCashActionRedirectUrl = getTokoCashActionRedirectUrl(tokoCashData
                .getDrawerTokoCashAction());
        headerTokoCashLabel.setText(tokoCashLabel);
        if (tokoCashData.getLink() == 1) {
            setOnClickListener(onMainViewClickedListener(tokoCashRedirectUrl));
            tokoCashAmount.setText(tokoCashData.getBalance());
            tokoCashButton.setText(getContext().getString(R.string.toko_cash_top_up));
            tokoCashButton.setOnClickListener(getTopUpClickedListenerHarcodedToNative(tokoCashActionRedirectUrl));
            pendingLayout.setVisibility(GONE);
            normalLayout.setVisibility(VISIBLE);
            if(showTopUpButton) tokoCashButton.setVisibility(VISIBLE);
            else tokoCashButton.setVisibility(GONE);
        } else {
            actionListener.onRequestPendingCashBack();
            tokoCashButton.setOnClickListener(onActivationClickedListener());
            tokoCashButton.setText(getContext().getString(R.string.toko_cash_activation));
            activationRedirectUrl = tokoCashRedirectUrl;
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

    public void activateTokoCashFromBottomSheet() {
        onActivationClickedListener();
    }

    @NonNull
    private OnClickListener getTopUpClickedListenerHarcodedToNative(
            final String tokoCashActionRedirectUrl) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onTopUpTokoCashClicked();
            }
        };
    }

    private String getTokoCashActionRedirectUrl(DrawerTokoCashAction drawerTokoCashAction) {
        if (drawerTokoCashAction == null) return "";
        else return drawerTokoCashAction.getRedirectUrl();
    }

    private OnClickListener onActivationClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onActivationTokoCashClicked();
            }
        };
    }

    private OnClickListener onTopUpClickedListener(final String redirectUrl) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                openTokoCashWebView(redirectUrl);
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

    private void openTokoCashWebView(String redirectURL) {
        if (getContext() instanceof Activity) {
            if (((Activity) getContext()).getApplication() instanceof TkpdCoreRouter) {
                ((TkpdCoreRouter) ((Activity) getContext()).getApplication())
                        .goToWallet(getContext(), redirectURL);
            }
        }
    }

    private OnClickListener onMainViewClickedListener(final String redirectUrl) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                openTokoCashWebView(redirectUrl);
            }
        };
    }

    public interface ActionListener {
        void onTopUpTokoCashClicked();

        void onActivationTokoCashClicked();

        void onRequestPendingCashBack();

        void onShowTokoCashBottomSheet();
    }

}
