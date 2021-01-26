package com.tokopedia.review.feature.reputationhistory.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.review.R;

/**
 * @author normansyahputa on 3/21/17.
 */

public class SellerReputationInfoActivity extends BaseSimpleActivity {

    WebView webviewReputationInfo;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWhiteStatusBar();

        webviewReputationInfo = (WebView) findViewById(R.id.webview_reputation_review_info);

        webviewReputationInfo.getSettings().setJavaScriptEnabled(true);
        webviewReputationInfo.loadUrl("file:///android_asset/poin-reputasi.html");
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_reputation_review_info;
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    private void setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
        }
    }

}
