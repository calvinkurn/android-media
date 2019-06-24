package com.tokopedia.webview;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.utils.URLGenerator;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.regex.Pattern;

public class BaseSessionWebViewFragment extends BaseWebViewFragment {
    public static final String ARGS_URL = "arg_url";
    private static final String PATTERN = "^((http|https)://m[.]tokopedia[.]com/).+";
    private static final String ERROR_MESSAGE = "Url tidak valid";
    private static final String CRASHLYTICS_ERROR_MESSAGE = "Invalid webview url - ";

    private UserSessionInterface userSession;
    private String url;
    public static final String TOKOPEDIA_STRING = "tokopedia";
    private boolean isTokopediaUrl;

    public static BaseSessionWebViewFragment newInstance(String url) {
        BaseSessionWebViewFragment fragment = new BaseSessionWebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(ARGS_URL);
        isTokopediaUrl = WebViewHelper.validateUrl(url);
        userSession = new UserSession(getActivity().getApplicationContext());
    }

    @Override
    protected String getUrl() {
        if (isTokopediaUrl) {
            String gcmId = userSession.getDeviceId();
            String userId = userSession.getUserId();
            return URLGenerator.generateURLSessionLogin(
                    Uri.encode(getPlainUrl()),
                    gcmId,
                    userId);
        } else {
            if(getActivity() != null){
                Crashlytics crashlytics = Crashlytics.getInstance();
                if(crashlytics != null)
                    crashlytics.log(CRASHLYTICS_ERROR_MESSAGE + url);

                NetworkErrorHelper.showRedSnackbar(getActivity(), ERROR_MESSAGE);
            }

            return null;
        }
    }

    @Override
    protected String getUserIdForHeader() {
        if (isTokopediaUrl) {
            return userSession.getUserId();
        }
        return null;
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        if (isTokopediaUrl) {
            return userSession.getAccessToken();
        }
        return null;
    }

    protected String getPlainUrl() {
        return url;
    }

    private Boolean validateWebviewUrl(String url){
        return Pattern.matches(PATTERN, url);
    }
}
