package com.tokopedia.tkpd.instoped.dialog;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.instoped.InstagramAuth;
import com.tokopedia.tkpd.instoped.InstopedService;
import com.tokopedia.tkpd.instoped.OnRequestTokenCodeListener;
import com.tokopedia.tkpd.myproduct.ProductActivity;
import com.tokopedia.tkpd.network.retrofit.utils.MapNulRemover;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tkpd_Eka on 4/6/2016.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class InstagramAuthenticationDialog extends Fragment implements ProductActivity.OnBackPressedListener {

    private InstagramAuth.OnRequestAccessTokenListener onRequestAccessTokenListener;
    private InstopedService service = new InstopedService();
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    public boolean onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return false;
        }
    }

    private class myWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == 100) {
                    view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    getActivity().setProgressBarIndeterminateVisibility(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public class InstagramWebViewClient extends WebViewClient {

        OnRequestTokenCodeListener listener;

        public InstagramWebViewClient(OnRequestTokenCodeListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("?code")) {
                Uri codeUri = Uri.parse(url);
                String code = codeUri.getQueryParameter("code");
                if (listener != null) {
                    listener.onSuccess(code);
                } else
                    throw new RuntimeException("Listener is null");
                view.stopLoading();
            } else {
                view.loadUrl(url);
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                getActivity().setProgressBarIndeterminateVisibility(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }


        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            CommonUtils.dumper("DEEPLINK " + errorCode + "  " + description + " " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        CookieManager cm = CookieManager.getInstance();
//        cm.removeAllCookie();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Login Instagram");
        View rootview = inflater.inflate(R.layout.instagram_auth_dialog, container, false);
        webView = (WebView) rootview.findViewById(R.id.webview_instagram);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        getActivity().setProgressBarIndeterminateVisibility(true);
        WebSettings ws = webView.getSettings();
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(false);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setSaveFormData(false);
        ws.setSavePassword(false);
        ws.setJavaScriptEnabled(true);
        webView.setWebViewClient(new InstagramWebViewClient(new OnRequestTokenCodeListener() {
            @Override
            public void onSuccess(String code) {
                postAccessTokenRequest(code);
            }
        }));

        webView.setWebChromeClient(new myWebChromeClient());
        webView.loadUrl("https://api.instagram.com/oauth/authorize/?client_id=" + InstagramAuth.CLIENT_ID + "&redirect_uri=" + InstagramAuth.CALLBACK_URL + "&response_type=code&scope=basic+public_content");
        webView.clearCache(true);
        return rootview;
    }

    public void postAccessTokenRequest(String code) {
        Observable<Response<String>> observable = service.getApi().getAccessToken(MapNulRemover.removeNull(getAccessTokenParams(code)));
        observable.subscribeOn(Schedulers.newThread()).unsubscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetAccessTokenSubscriber());
    }

    private Map<String, String> getAccessTokenParams(String code) {
        Map<String, String> params = new ArrayMap<>();
        params.put("client_id", InstagramAuth.CLIENT_ID);
        params.put("client_secret", InstagramAuth.CLIENT_SECRET);
        params.put("redirect_uri", InstagramAuth.CALLBACK_URL);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        return params;
    }

    private Subscriber<Response<String>> onGetAccessTokenSubscriber() {
        return new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<String> s) {
                if (onRequestAccessTokenListener != null) {
                    onRequestAccessTokenListener.onSuccess(s.body(), getActivity().getSupportFragmentManager());
//                    dismiss();
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        super.onDestroyView();
    }

    public void setOnRequestAccessTokenListener(InstagramAuth.OnRequestAccessTokenListener listener) {
        this.onRequestAccessTokenListener = listener;
    }


}
