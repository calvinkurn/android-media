package com.tokopedia.tkpd.redirect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.tkpd.R;

public class RedirectCreateShopActivity extends TActivity {

    private static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";
    private static final String MARKET_URL = "market://details?id=";

    @Override
    public String getScreenName() {
        return AppScreen.CREATE_SHOP_REDIRECT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_redirect_create_shop);
        getSupportActionBar().setTitle("");
        findViewById(R.id.button_redirect)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(MARKET_URL + TOP_SELLER_APPLICATION_PACKAGE)));
                        UnifyTracking.eventDownloadFromSwitcher();
                    }
                });
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RedirectCreateShopActivity.class);
    }
}
