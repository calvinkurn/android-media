package com.tokopedia.tkpd.goldmerchant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.tkpd.R;

/**
 * Created by kris on 2/6/17. Tokopedia
 */

public class GoldMerchantRedirectActivity extends TActivity{
    @Override
    public String getScreenName() {
        return null;
    }
    private static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_gold_merchant_redirect);
        TextView proceedButton = (TextView) findViewById(R.id.proceed_button);
        TextView backButton = (TextView) findViewById(R.id.back_button);
        proceedButton.setOnClickListener(onProceedButtonClicked());
        backButton.setOnClickListener(onBackButtonClickedListener());
    }

    private View.OnClickListener onBackButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }
    private View.OnClickListener onProceedButtonClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager()
                        .getLaunchIntentForPackage(TOP_SELLER_APPLICATION_PACKAGE);
                if(launchIntent != null) startActivity(launchIntent);
                else startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + TOP_SELLER_APPLICATION_PACKAGE)));
            }
        };
    }

}
