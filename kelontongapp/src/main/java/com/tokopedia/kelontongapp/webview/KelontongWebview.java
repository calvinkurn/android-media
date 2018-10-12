package com.tokopedia.kelontongapp.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by meta on 12/10/18.
 */
public class KelontongWebview extends WebView {

    private static final String PARAM_URL = "url";
    private static final String FORMAT_UTF_8 = "UTF-8";

    public KelontongWebview(Context context) {
        super(context);
    }

    public KelontongWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KelontongWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}