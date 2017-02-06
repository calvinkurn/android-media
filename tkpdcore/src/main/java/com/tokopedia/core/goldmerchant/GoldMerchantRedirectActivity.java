package com.tokopedia.core.goldmerchant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.app.TActivity;


/**
 * Created by kris on 2/1/17. Tokopedia
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
        proceedButton.setOnClickListener(onProceedButtonClicked());
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
