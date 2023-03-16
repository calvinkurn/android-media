package com.tokopedia.tkpd.redirect;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.track.TrackApp;

public class RedirectCreateShopActivity extends AppCompatActivity {

    private static final String PACKAGE_SELLER_APP = "com.tokopedia.sellerapp";
    private static final String APPLINK_PLAYSTORE = "market://details?id=";
    private static final String URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";

    @Override
    protected void onResume() {
        super.onResume();
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(AppScreen.CREATE_SHOP_REDIRECT);
    }

    @SuppressLint("ResourcePackage")
    private void setStatusBar(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green_600));
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect_create_shop);
        setStatusBar();
        setToolbar();
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

                        eventDownloadFromSwitcher();
                    }
                });
    }

    public void eventDownloadFromSwitcher() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.SWITCHER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DOWNLOAD_APP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
