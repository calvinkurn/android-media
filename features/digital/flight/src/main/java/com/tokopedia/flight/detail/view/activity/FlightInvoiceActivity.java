package com.tokopedia.flight.detail.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

public class FlightInvoiceActivity extends BaseWebViewActivity {
    public static final String URL = "WEBVIEW_URL";
    private String url;

    public static Intent newInstance(Context context, String url) {
        Intent intent = new Intent(context, FlightInvoiceActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(URL);
        }
        return BaseSessionWebViewFragment.newInstance(url);
    }

    @Nullable
    @Override
    protected Intent getContactUsIntent() {
        return null;
    }
}