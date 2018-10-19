package com.tokopedia.kelontongapp.webview;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import static android.app.Activity.RESULT_OK;

public class KelontongWebChromeClient extends WebChromeClient {

    private static final int PROGRESS_COMPLETED = 100;

    private ProgressBar progressBar;

    public KelontongWebChromeClient(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == PROGRESS_COMPLETED && progressBar != null) {
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        super.onProgressChanged(view, newProgress);
    }
}
