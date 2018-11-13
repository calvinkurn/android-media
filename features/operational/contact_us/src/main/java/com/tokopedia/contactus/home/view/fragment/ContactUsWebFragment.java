package com.tokopedia.contactus.home.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.base.view.webview.CommonWebViewClient;
import com.tokopedia.abstraction.base.view.webview.FilePickerInterface;
import com.tokopedia.abstraction.base.view.webview.TkpdWebView;
import com.tokopedia.abstraction.base.view.webview.TkpdWebViewClient;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.contactus.BuildConfig;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactUsWebFragment extends TkpdBaseV4Fragment implements FilePickerInterface {

    private static String PARAM_URL = "PARAM_URL";
    private static final String SOLUTION_ID = "solution_id";
    private static final String TAGS = "tags";
    private static final String ORDER_ID = "order_id";
    private static final String APPLINK_SCHEME = "tokopedia://";
    private static final String CHATBOT_SCHEME = "tokopedia://topchat";
    private static final String SEAMLESS_LOGIN = "seamless?";
    private String URL;

    @BindView(R2.id.scroll_view)
    ScrollView mainView;

    @BindView(R2.id.webview)
    TkpdWebView webView;

    @BindView(R2.id.progressbar)
    ProgressBar progressBar;

    private UserSessionInterface userSession;

    public ContactUsWebFragment() {

    }

    public static ContactUsWebFragment getWebViewFragment(String Url) {
        ContactUsWebFragment contactUsWebFragment = new ContactUsWebFragment();
        Bundle arguments = new Bundle();
        arguments.putString(PARAM_URL, Url);
        contactUsWebFragment.setArguments(arguments);
        return contactUsWebFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getString(PARAM_URL, "").equals("")) {
            URL = TkpdBaseURL.ContactUs.URL_HELP;
        } else
            URL = getArguments().getString(PARAM_URL);
        userSession = new UserSession(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_us_webview, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private void initView() {
        if (webView != null) {
            webView.clearCache(true);
        }
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new CommonWebViewClient(this, progressBar));
        progressBar.setIndeterminate(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAppCacheEnabled(false);
        MethodChecker.setAllowMixedContent(webSettings);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadAuthUrlWithFlags(URL, userSession.getUserId(), userSession.getAccessToken());
    }

    private class MyWebClient extends TkpdWebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                progressBar.setIndeterminate(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mainView != null)
                mainView.postDelayed(() -> {
                    if (mainView != null)
                        mainView.smoothScrollTo(0, 0);
                }, 300);
        }

        @Override
        protected boolean onOverrideUrl(Uri url) {
            try {
                if (url.getLastPathSegment().equals("contact-us.pl")) {
                    webView.loadAuthUrlWithFlags(generateURLContactUs(TkpdBaseURL
                            .BASE_CONTACT_US), userSession.getUserId(), userSession.getAccessToken());
                    return true;
                } else if (url.getQueryParameter("action") != null &&
                        url.getQueryParameter("action").equals("create_ticket")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactUsActivity.PARAM_SOLUTION_ID,
                            url.getQueryParameter(SOLUTION_ID) == null ? "" : url
                                    .getQueryParameter(SOLUTION_ID));
                    bundle.putString(ContactUsActivity.PARAM_TAG,
                            url.getQueryParameter(TAGS) == null ? "" : url.getQueryParameter(TAGS));
                    bundle.putString(ContactUsActivity.PARAM_ORDER_ID,
                            url.getQueryParameter(ORDER_ID) == null ? "" : url.getQueryParameter
                                    (ORDER_ID));
                    return true;
                } else if (url.getQueryParameter("action") != null &&
                        url.getQueryParameter("action").equals("return")) {
                    Toast.makeText(getActivity(), getString(R.string
                            .finish_contact_us), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return true;
                } else if (url.toString().contains(CHATBOT_SCHEME)
                        && getActivity().getApplicationContext() instanceof ContactUsModuleRouter) {
                    String messageId = url.getLastPathSegment();
                    Intent chatBotIntent = ((ContactUsModuleRouter) getActivity()
                            .getApplicationContext())
                            .getChatBotIntent(getActivity(), messageId);
                    startActivity(chatBotIntent);
                    return true;
                } else if (url.toString().contains(APPLINK_SCHEME)
                        && getActivity().getApplicationContext() instanceof ContactUsModuleRouter) {
                    ((ContactUsModuleRouter) getActivity().getApplicationContext())
                            .actionNavigateByApplinksUrl(getActivity(), url.toString(), new
                                    Bundle());
                    return true;
                } else {
                    return false;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }

        }

    }

    private String generateURLContactUs(String url) {
        return getBaseUrl() + SEAMLESS_LOGIN
                + "token=" + getRegistrationID(getActivity())
                + "&os_type=1"
                + "&uid=" + userSession.getUserId()
                + "&url=" + url;
    }

    private String getBaseUrl() {
        String baseUrl = TkpdBaseURL.JS_DOMAIN;
        if (BuildConfig.DEBUG) {
            SharedPreferences pref = getActivity().getApplicationContext()
                    .getSharedPreferences("DOMAIN_WS_4", Context.MODE_PRIVATE);
            if (pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN).contains("alpha")) {
                baseUrl = TkpdBaseURL.JS_ALPHA_DOMAIN;
            } else if (pref.getString("DOMAIN_WS4", TkpdBaseURL.BASE_DOMAIN).contains("staging")) {
                baseUrl = TkpdBaseURL.JS_STAGING_DOMAIN;
                ;
            }
        }
        return baseUrl;
    }

    private String getRegistrationID(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("GCM_STORAGE", Context.MODE_PRIVATE);
        return sharedPrefs.getString("gcm_id", "");

    }
}


