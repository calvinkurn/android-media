package com.tokopedia.core.loyaltysystem;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;

public class LoyaltyDetail extends TActivity implements FragmentGeneralWebView.OnFragmentInteractionListener {

    public static int FRAGMENT_VIEW = R.id.main_view;
    private String Url;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_LOYALTY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_loyalty_detail);
        Url = getIntent().getExtras().getString("url");
        if (Url.startsWith("https://pulsa.tokopedia.com/saldo/"))
            toolbar.setTitle(R.string.title_activity_people_deposit);
        else
            toolbar.setTitle(R.string.title_activity_loyalty_detail);

        Log.i("LOYALTY", Url);
        showFragmentWebView();
    }

    private void showFragmentWebView() {
        Fragment fragment = FragmentGeneralWebView.createInstance(Url);
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
