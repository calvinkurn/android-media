package com.tokopedia.webview;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class WebViewActivity extends BaseSimpleActivity {

    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String TOKOPEDIA_URL = "https://www.tokopedia.com/";

    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            url = uri.getQueryParameter(URL);
            title = uri.getQueryParameter(TITLE);
        }
        if (TextUtils.isEmpty(url)) {
            url = TOKOPEDIA_URL;
        }
        if (TextUtils.isEmpty(title)) {
            title = getTitle().toString();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        updateTitle(title);
        return BaseSessionWebViewFragment.newInstance(url);
    }
}
