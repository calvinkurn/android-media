package com.tokopedia.tkpd.loyaltysystem;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.webview.fragment.FragmentGeneralWebView;

public class LoyaltyDetail extends TActivity implements FragmentGeneralWebView.OnFragmentInteractionListener {

    public static int FRAGMENT_VIEW = R.id.main_view;
    private String Url;

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
