package com.tokopedia.contactus.createticket.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity.BackButtonListener;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.core.util.TkpdWebViewClient;

import butterknife.BindView;

import static com.tokopedia.contactus.createticket.ContactUsConstant.EXTRAS_PARAM_URL;
import static android.app.Activity.RESULT_OK;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsFaqFragment extends BasePresenterFragment {

    private static final String SOLUTION_ID = "solution_id";
    private static final String TAGS = "tags";
    private static final String ORDER_ID = "order_id";
    private static final String APPLINK_SCHEME = "tokopedia://";
    private static final String CHATBOT_SCHEME = "tokopedia://topchat";
    private ValueCallback<Uri> uploadMessageBeforeLolipop;
    public ValueCallback<Uri[]> uploadMessageAfterLolipop;
    public final static int ATTACH_FILE_REQUEST = 1;


    @BindView(R2.id.scroll_view)
    ScrollView mainView;

    @BindView(R2.id.webview)
    TkpdWebView webView;

    @BindView(R2.id.progressbar)
    ProgressBar progressBar;

    ContactUsFaqListener listener;
    String url;

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
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        String url;
        if (getArguments().getString(EXTRAS_PARAM_URL, "").equals("")) {
            url = TkpdBaseURL.ContactUs.URL_HELP;
        } else
            url = getArguments().getString(EXTRAS_PARAM_URL);

        webView.loadAuthUrlWithFlags(url);


    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_contact_us_faq;
    }

    @Override
    protected void initView(View view) {
        if (webView != null) {
            webView.clearCache(true);
        }
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebViewClient());
        progressBar.setIndeterminate(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAppCacheEnabled(false);
        MethodChecker.setAllowMixedContent(webSettings);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        listener = (ContactUsActivity) getActivity();
    }

    @Override
    protected void setActionVar() {

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
                    progressBar.setIndeterminate(false);
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

    private void openFileChooserBeforeLolipop(ValueCallback<Uri> uploadMessage){
        uploadMessageBeforeLolipop = uploadMessage;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
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
                mainView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mainView != null)
                            mainView.smoothScrollTo(0, 0);
                    }
                }, 300);
        }

        @Override
        protected boolean onOverrideUrl(Uri url) {
            try {
                if (url.getLastPathSegment().equals("contact-us.pl")) {
                    webView.loadAuthUrlWithFlags(URLGenerator.generateURLContactUs(TkpdBaseURL
                            .BASE_CONTACT_US, context));
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
                    CommonUtils.UniversalToast(getActivity(), getString(R.string
                            .finish_contact_us));
                    getActivity().finish();
                    return true;
                } else if (url.toString().contains(CHATBOT_SCHEME)
                        && getActivity().getApplicationContext() instanceof ContactUsModuleRouter) {
                    String messageId = url.getLastPathSegment();
                    Intent chatBotIntent = ((ContactUsModuleRouter) getActivity()
                            .getApplicationContext())
                            .getChatBotIntent(context, messageId);
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
