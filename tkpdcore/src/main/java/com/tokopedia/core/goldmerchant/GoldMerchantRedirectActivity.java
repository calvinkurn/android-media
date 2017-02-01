package com.tokopedia.core.goldmerchant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.TActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kris on 2/1/17. Tokopedia
 */


public class GoldMerchantRedirectActivity extends TActivity{
    @Override
    public String getScreenName() {
        return null;
    }
    private static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";

    @BindView(R2.id.proceed_button)
    TextView proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_merchant_redirect);
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.proceed_button)
    void onProceedButtonClicked() {
        Intent launchIntent = getPackageManager()
                .getLaunchIntentForPackage(TOP_SELLER_APPLICATION_PACKAGE);
        if(launchIntent != null) startActivity(launchIntent);
        else startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + TOP_SELLER_APPLICATION_PACKAGE)));
    }
}
