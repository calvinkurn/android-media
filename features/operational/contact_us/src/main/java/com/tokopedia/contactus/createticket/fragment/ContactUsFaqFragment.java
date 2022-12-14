package com.tokopedia.contactus.createticket.fragment;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.contactus.createticket.ContactUsConstant.EXTRAS_PARAM_URL;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity.BackButtonListener;
import com.tokopedia.contactus.createticket.utils.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.unifycomponents.LoaderUnify;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.webview.TkpdWebView;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsFaqFragment extends TkpdBaseV4Fragment {

    private static final String SOLUTION_ID = "solution_id";
    private static final String TAGS = "tags";
    private static final String ORDER_ID = "order_id";
    private static final String APPLINK_SCHEME = "tokopedia://";
    private static final String CHATBOT_SCHEME = "tokopedia://topchat";
    private ValueCallback<Uri> uploadMessageBeforeLolipop;
    public ValueCallback<Uri[]> uploadMessageAfterLolipop;
    public final static int ATTACH_FILE_REQUEST = 1;
    private TkpdWebView webView;
    private LoaderUnify progressBar;
    public static final String URL_HELP = TokopediaUrl.Companion.getInstance().getWEB() + "help?utm_source=android";
    private UserSessionInterface session;
    ContactUsFaqListener listener;
    String url;
    private Bundle savedState;

    @Override
    protected String getScreenName() {
        return null;
    }

    public interface ContactUsFaqListener {
        void onGoToCreateTicket(Bundle solutionId);
    }

    public static ContactUsFaqFragment createInstance(Bundle extras) {
        ContactUsFaqFragment fragment = new ContactUsFaqFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initialVar();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lauchWebView();
    }

    private void lauchWebView() {
        String url;
        if (getArguments().getString(EXTRAS_PARAM_URL, "").equals("")) {
            url = URL_HELP;
        } else
            url = getArguments().getString(EXTRAS_PARAM_URL);

        webView.loadAuthUrlWithFlags(url, session);
    }

    private int getFragmentLayout() {
        return R.layout.fragment_contact_us_faq;
    }

    private void initView(View view) {
        if (webView != null) {
            webView.clearCache(true);
        }
        webView = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progressbar);

        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebViewClient());
        progressBar.setVisibility(View.VISIBLE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
//        webSettings.setAppCacheEnabled(false);
        MethodChecker.setAllowMixedContent(webSettings);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void initialVar() {
        listener = (ContactUsActivity) getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == ATTACH_FILE_REQUEST) {
                    if (null == uploadMessageAfterLolipop) {
                        return;
                    }

                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};

                    }
                }
            }
            uploadMessageAfterLolipop.onReceiveValue(results);
            uploadMessageAfterLolipop = null;
        } else {
            if (requestCode == ATTACH_FILE_REQUEST) {
                if (null == uploadMessageBeforeLolipop) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                uploadMessageBeforeLolipop.onReceiveValue(result);
                uploadMessageBeforeLolipop = null;
            }
        }
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == 100) {
                    webView.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onProgressChanged(view, newProgress);
        }

        //For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooserBeforeLolipop(uploadMsg);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            openFileChooserBeforeLolipop(uploadMsg);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooserBeforeLolipop(uploadMsg);
        }

        //For Android 5.0+
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
            if (uploadMessageAfterLolipop != null) {
                uploadMessageAfterLolipop.onReceiveValue(null);
            }
            uploadMessageAfterLolipop = filePathCallback;

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("*/*");
            Intent[] intentArray = new Intent[0];

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, ATTACH_FILE_REQUEST);
            return true;

        }
    }

    private void openFileChooserBeforeLolipop(ValueCallback<Uri> uploadMessage) {
        uploadMessageBeforeLolipop = uploadMessage;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return onOverrideUrl(uri);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return onOverrideUrl(request.getUrl());
        }

        protected boolean onOverrideUrl(Uri url) {
            try {
                Context mContext = getContext();
                if (url.getLastPathSegment().equals("contact-us.pl")) {
                    if (mContext != null) {
                        webView.loadAuthUrlWithFlags(URLGenerator.generateURLContactUs(TkpdBaseURL
                                .BASE_CONTACT_US, mContext), session);
                    }
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
                    listener.onGoToCreateTicket(bundle);
                    return true;
                } else if (url.getQueryParameter("action") != null &&
                        url.getQueryParameter("action").equals("return")) {
                    Toast.makeText(getActivity(), MethodChecker.fromHtml(getString(R.string.finish_contact_us)), Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    return true;
                } else if (url.toString().contains(CHATBOT_SCHEME)) {
                    String messageId = url.getLastPathSegment();
                    Intent chatBotIntent = RouteManager.getIntent(getContext(), ApplinkConst.CHATBOT
                            .replace(String.format("{%s}", ApplinkConst.Chat.MESSAGE_ID), messageId));
                    startActivity(chatBotIntent);
                    return true;
                } else if (url.toString().contains(APPLINK_SCHEME)) {
                    RouteManager.route(getActivity(), url.toString());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressBar != null)
            progressBar.setIndeterminate(false);
    }

    public BackButtonListener getBackButtonListener() {
        return new BackButtonListener() {
            @Override
            public void onBackPressed() {
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                }

            }

            @Override
            public boolean canGoBack() {
                return webView != null && webView.canGoBack();
            }
        };
    }
}
