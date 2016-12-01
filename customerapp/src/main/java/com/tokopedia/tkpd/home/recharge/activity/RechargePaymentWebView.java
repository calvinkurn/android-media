package com.tokopedia.tkpd.home.recharge.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.tkpd.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.tkpd.home.recharge.view.widget.FragmentRechargeWebView;


/**
 * @author Kulomady on 7/22/2016.
 */
public class RechargePaymentWebView extends TActivity
        implements FragmentGeneralWebView.OnFragmentInteractionListener{

    private FragmentRechargeWebView fragment;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RECHARGE_PAYMENT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_webview_container);
        String url = getIntent().getExtras().getString("url");
        fragment = FragmentRechargeWebView.createInstance(url);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }
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

//    @Override
//    public void catchToWebView(String url) {
//        FragmentBannerWebView fragment = FragmentBannerWebView.createInstance(url);
//        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
//    }

    @Override
    public void onBackPressed() {
        if (fragment.getWebviewRecharge().canGoBack()) {
            fragment.getWebviewRecharge().goBack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
