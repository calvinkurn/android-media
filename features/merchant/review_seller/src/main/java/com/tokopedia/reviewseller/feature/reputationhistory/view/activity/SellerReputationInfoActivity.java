package com.tokopedia.reviewseller.feature.reputationhistory.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.reviewseller.R;

/**
 * @author normansyahputa on 3/21/17.
 */

public class SellerReputationInfoActivity extends TActivity {

    WebView webviewReputationInfo;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_reputation_info);
        webviewReputationInfo = (WebView) findViewById(R.id.webview_reputation_info);

        webviewReputationInfo.getSettings().setJavaScriptEnabled(true);
        webviewReputationInfo.loadUrl("file:///android_asset/poin-reputasi.html");
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
