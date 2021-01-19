package com.tokopedia.tkpd.goldmerchant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.tkpd.deeplink.activity.TActivity;
import com.tokopedia.track.TrackApp;

import static com.tokopedia.gm.common.constant.GMCommonConstantKt.GM_TITLE;

/**
 * Created by kris on 2/6/17. Tokopedia
 * Deeplink: GOLD_MERCHANT_REDIRECT
 */

public class GoldMerchantRedirectActivity extends TActivity {
    private static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";

    @Override
    public String getScreenName() {
        return AppScreen.GOLD_MERCHANT_REDIRECT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_gold_merchant_redirect);
        TextView proceedButton = findViewById(R.id.proceed_button);
        TextView backButton = findViewById(R.id.back_button);
        proceedButton.setOnClickListener(onProceedButtonClicked());
        backButton.setOnClickListener(onBackButtonClickedListener());

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.title_gold_merchant_redirect, GM_TITLE));

        getSupportActionBar().setTitle(GM_TITLE);
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
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(TOP_SELLER_APPLICATION_PACKAGE);
                if (launchIntent != null) {
                    eventClickGMSwitcher(AppEventTracking.EventLabel.OPEN_TOP_SELLER + AppEventTracking.EventLabel.OPEN_APP);
                    startActivity(launchIntent);
                } else {
                    eventClickGMSwitcher(AppEventTracking.EventLabel.OPEN_TOP_SELLER + AppEventTracking.EventLabel.DOWNLOAD_APP);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + TOP_SELLER_APPLICATION_PACKAGE)));
                }
            }
        };
    }

    public void eventClickGMSwitcher(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.TOP_SELLER,
                AppEventTracking.Category.GM_SWITCHER,
                AppEventTracking.Action.CLICK,
                label);
    }

}
