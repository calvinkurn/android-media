package com.tokopedia.core.loyaltysystem;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;

import retrofit2.http.Url;

public class LoyaltyDetail extends TActivity implements FragmentGeneralWebView.OnFragmentInteractionListener {

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
        Fragment fragment = FragmentGeneralWebView.createInstance(url);
        getFragmentManager().beginTransaction().add(FRAGMENT_VIEW, fragment).commit();
    }

    @Override
    public void onWebViewSuccessLoad() {
    }

    @Override
    public void onWebViewErrorLoad() {
    }

    @Override
    public void onWebViewProgressLoad() {
    }
}
