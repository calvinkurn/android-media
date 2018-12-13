package com.tokopedia.tkpd.redirect;

import android.content.Context;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.tkpd.R;

public class RedirectCreateShopActivity extends TActivity {

    private static final String PACKAGE_SELLER_APP = "com.tokopedia.sellerapp";
    private static final String APPLINK_PLAYSTORE = "market://details?id=";
    private static final String URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";

    @Override
    public String getScreenName() {
        return AppScreen.CREATE_SHOP_REDIRECT;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return false;
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
                        try {
                            RedirectCreateShopActivity.this.startActivity(
                                    new Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)
                                    )
                            );
                        } catch (ActivityNotFoundException anfe) {
                            RedirectCreateShopActivity.this.startActivity(
                                    new Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)
                                    )
                            );
                        }

                        UnifyTracking.eventDownloadFromSwitcher(view.getContext());
                    }
                });
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RedirectCreateShopActivity.class);
    }
}
