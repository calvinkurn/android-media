package com.tokopedia.sellerapp.deeplink;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkPresenterImpl;

/**
 * Created by Herdi_WORK on 10.05.17.
 */

public class DeepLinkActivity extends AppCompatActivity {
    private Uri uriData;
    private static final String EXTRA_STATE_APP_WEB_VIEW = "EXTRA_STATE_APP_WEB_VIEW";
    private DeepLinkPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getData() != null) {
            setupURIPass(getIntent().getData());
        }
        presenter = new DeepLinkPresenterImpl(this);
        initDeepLink();
    }

    private void setupURIPass(Uri data) {
        uriData = data;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initDeepLink() {
        if (uriData != null || getIntent().getBooleanExtra(EXTRA_STATE_APP_WEB_VIEW, false)) {
            presenter.processDeepLinkAction(this, uriData);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        SplitCompat.installActivity(this);
    }
}
