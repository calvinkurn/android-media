package com.tokopedia.tkpd.topads;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;

public class WebViewTopAdsActivity extends TActivity {

    WebViewTopAdsFragment fragment;
    FragmentManager manager;
    BackButtonListener listener;

    public interface BackButtonListener {
        void onBackPressed();

        boolean canGoBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_web_view_top_ads);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            fragment = WebViewTopAdsFragment.newInstance(bundle.getString(WebViewTopAdsFragment.SOURCE_EXTRA));
            manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_right);
            transaction.add(R.id.main_view, fragment, "first");
            transaction.commit();

            listener = fragment.getOnBackPressedListener();
        }
    }

    @Override
    public void onBackPressed() {
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        } else if (listener.canGoBack()) {
            listener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}