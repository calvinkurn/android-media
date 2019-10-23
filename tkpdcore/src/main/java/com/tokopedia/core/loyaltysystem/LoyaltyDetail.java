package com.tokopedia.core.loyaltysystem;

import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core2.R;
import com.tokopedia.webview.BaseSessionWebViewFragment;

public class LoyaltyDetail extends TActivity {

    public static int FRAGMENT_VIEW = R.id.main_view;
    private String url;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_LOYALTY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_loyalty_detail);
        url = getIntent().getExtras().getString("url");
        if (url != null && url.contains("https://pulsa.tokopedia.com/saldo/"))
            getSupportActionBar().setTitle(R.string.title_activity_people_deposit);
        else
            getSupportActionBar().setTitle(R.string.title_activity_loyalty_detail);

        showFragmentWebView();
    }

    private void showFragmentWebView() {
        getSupportFragmentManager().beginTransaction().add(FRAGMENT_VIEW, BaseSessionWebViewFragment.newInstance(url)).commit();
    }


}
