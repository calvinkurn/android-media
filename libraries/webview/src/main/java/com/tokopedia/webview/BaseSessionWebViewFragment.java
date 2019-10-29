package com.tokopedia.webview;

import android.os.Bundle;

import androidx.annotation.NonNull;

import static com.tokopedia.webview.ConstantKt.KEY_ALLOW_OVERRIDE;
import static com.tokopedia.webview.ConstantKt.KEY_NEED_LOGIN;
import static com.tokopedia.webview.ConstantKt.KEY_URL;

public class BaseSessionWebViewFragment extends BaseWebViewFragment {

    @NonNull
    public static BaseSessionWebViewFragment newInstance(@NonNull String url) {
        BaseSessionWebViewFragment fragment = new BaseSessionWebViewFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    public static BaseSessionWebViewFragment newInstance(@NonNull String url,
                                                         boolean needLogin,
                                                         boolean overrideUrl) {
        BaseSessionWebViewFragment fragment = new BaseSessionWebViewFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        args.putBoolean(KEY_NEED_LOGIN, needLogin);
        args.putBoolean(KEY_ALLOW_OVERRIDE, overrideUrl);
        fragment.setArguments(args);
        return fragment;
    }

}
