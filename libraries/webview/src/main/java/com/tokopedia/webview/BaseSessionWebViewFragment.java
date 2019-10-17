package com.tokopedia.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.url.TokopediaUrl;

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
