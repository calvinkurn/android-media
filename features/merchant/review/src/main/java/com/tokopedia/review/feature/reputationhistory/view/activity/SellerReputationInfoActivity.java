package com.tokopedia.review.feature.reputationhistory.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;

import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.review.R;

/**
 * @author normansyahputa on 3/21/17.
 */

public class SellerReputationInfoActivity extends BaseActivity {

    WebView webviewReputationInfo;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reputation_review_info);
        webviewReputationInfo = (WebView) findViewById(R.id.webview_reputation_review_info);

        webviewReputationInfo.getSettings().setJavaScriptEnabled(true);
        webviewReputationInfo.loadUrl("file:///android_asset/poin-reputasi.html");
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
