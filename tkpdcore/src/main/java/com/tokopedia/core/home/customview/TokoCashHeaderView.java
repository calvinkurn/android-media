package com.tokopedia.core.home.customview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
        String tokoCashActionRedirectUrl = getTokoCashActionRedirectUrl(tokoCashData);
        if(tokoCashData.getData().getLink().equals(1)) {
            setOnClickListener(onMainViewClickedListener(tokoCashRedirectUrl));
            tokoCashAmount.setText(tokoCashData.getData().getBalance());
            tokoCashButton.setText(getContext().getString(R.string.toko_cash_top_up));
            tokoCashButton.setOnClickListener(onTopUpClickedListener(tokoCashActionRedirectUrl));
        } else {
            tokoCashButton.setOnClickListener(onActivationClickedListener(
                    tokoCashActionRedirectUrl
            ));
            tokoCashButton.setText(getContext().getString(R.string.toko_cash_activation));
        }
    }

    private String getTokoCashActionRedirectUrl(TopCashItem tokoCashData) {
        if(tokoCashData.getData().getAction() == null) return "";
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

        tokoCashAmount = (TextView) findViewById(R.id.header_toko_cash_amount);
        tokoCashButton = (TextView) findViewById(R.id.header_toko_cash_button);
    }

    private void openTokoCashWebView(String redirectURL) {
        Bundle bundle = new Bundle();
        bundle.putString(TOKO_CASH_URL, redirectURL);
        if(getContext() instanceof Activity){
            if(((Activity) getContext()).getApplication() instanceof TkpdCoreRouter) {
                ((TkpdCoreRouter)((Activity) getContext()).getApplication())
                        .goToWallet(getContext(), bundle);
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

}
