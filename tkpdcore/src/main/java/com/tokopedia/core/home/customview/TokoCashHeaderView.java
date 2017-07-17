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
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;

/**
 * Created by kris on 4/21/17. Tokopedia
 */

public class TokoCashHeaderView extends RelativeLayout {

    private static final String TOKO_CASH_URL = "url";
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

    public void renderData(final TopCashItem tokoCashData) {
        String tokoCashRedirectUrl = tokoCashData.getData().getRedirectUrl();
        final String tokoCashActionRedirectUrl = getTokoCashActionRedirectUrl(tokoCashData);
        if (tokoCashData.getData().getLink().equals(1)) {
            setOnClickListener(onMainViewClickedListener(tokoCashRedirectUrl));
            tokoCashAmount.setText(tokoCashData.getData().getBalance());
            tokoCashButton.setText(getContext().getString(R.string.toko_cash_top_up));
            tokoCashButton.setOnClickListener(getTopUpClickedListenerHarcodedToNative(tokoCashActionRedirectUrl));
        } else {
            actionListener.onRequestPendingCashBack();
            tokoCashButton.setOnClickListener(onActivationClickedListener(
                    tokoCashActionRedirectUrl
            ));
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
        onActivationClickedListener(activationRedirectUrl);
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

    private String getTokoCashActionRedirectUrl(TopCashItem tokoCashData) {
        if (tokoCashData.getData().getAction() == null) return "";
        else return tokoCashData.getData().getAction().getRedirectUrl();
    }

    private OnClickListener onActivationClickedListener(final String redirectUrl) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                String seamlessUrl;
                seamlessUrl = URLGenerator.generateURLSessionLogin((Uri.encode(redirectUrl)),
                        getContext());
                openTokoCashWebView(seamlessUrl);
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
