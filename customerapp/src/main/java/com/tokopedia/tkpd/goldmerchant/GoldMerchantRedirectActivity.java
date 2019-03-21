package com.tokopedia.tkpd.goldmerchant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.track.TrackApp;

/**
 * Created by kris on 2/6/17. Tokopedia
 */

public class GoldMerchantRedirectActivity extends TActivity{
    @Override
    public String getScreenName() {
        return AppScreen.GOLD_MERCHANT_REDIRECT;
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

        TextView title = findViewById(R.id.title);
	String gm = getString(GMConstant.getGMTitleResource(this));
	title.setText(getString(R.string.title_gold_merchant_redirect, gm));

	getSupportActionBar().setTitle(gm);
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
                if(launchIntent != null){
                    eventClickGMSwitcher(AppEventTracking.EventLabel.OPEN_TOP_SELLER+AppEventTracking.EventLabel.OPEN_APP);
                    startActivity(launchIntent);
                }
                else {
                    eventClickGMSwitcher(AppEventTracking.EventLabel.OPEN_TOP_SELLER+AppEventTracking.EventLabel.DOWNLOAD_APP);
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
