package com.tokopedia.core.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.core.R;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tkpd_Eka on 5/25/2015.
 */

public class CreditCardWebView {

    public interface OnRedirectListener{
        void onRedirect(boolean success);
    }

    private class VtWebViewClient extends WebViewClient {

        boolean redirect = false;

        @Override
        public void onPageFinished(WebView view, String url) {
//            view.loadUrl("javascript:HTMLOUT.showHTML(document.documentElement.outerHTML);"); JANGAN DI DELETE untuk referensi seandainya butuh!!
            if(url.contains("token") && url.contains("callback") && redirect){
                dialog.dismiss();
                listener.onRedirect(true);
            }else if(url.contains("token") && url.contains("callback")){
                redirect = true;
                counterHandler.post(runnable);
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(url.contains("/callback/") && redirect){
                dialog.dismiss();
                listener.onRedirect(true);
            }
/*            if(url.contains("/callback/"))
                redirect = true;*/
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }
    }

    public static class MyJavaScriptInterface
    {
        @JavascriptInterface
        public void showHTML(String html)
        {
            System.out.print("Magic : " + html); // JANGAN DI DELETE emang untuk print hasil HTML
        }
    }

    public static class PostParam{
        public List<String> key = new ArrayList<>();
        public List<String> value = new ArrayList<>();

        public void addParam(String key, String value){
            this.key.add(key);
            this.value.add(value);
        }

        public String getParams(){
            String params = key.get(0) + "=" + value.get(0);
            int total = key.size();
            for(int i = 1 ; i < total ; i++){
                params = params + "&" + key.get(i) + "=" + value.get(i);
            }
            return params;
        }
    }

    private Context context;
    private String url;
    private WebView webView;
    private OnRedirectListener listener;
    private Dialog dialog;
    private int timeoutCounter;
    private Handler counterHandler;

    public CreditCardWebView(Context context, String URL){
        this.context = context;
        this.url = URL;
        counterHandler = new Handler();
    }

    public void setListener(OnRedirectListener listener){
        this.listener = listener;
    }

    public void showDialog(){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_web_view);
        webView = (WebView) dialog.findViewById(R.id.view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new VtWebViewClient());
        webView.loadUrl(url);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (webView.getUrl().contains("/callback/"))
                    listener.onRedirect(true);
            }
        });
        dialog.show();
    }

    public void postDialog(String postData){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_web_view);
        webView = (WebView) dialog.findViewById(R.id.view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new VtWebViewClient());
        webView.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));
        dialog.show();
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeoutCounter++;
            if(timeoutCounter == 10){
                dialog.dismiss();
                listener.onRedirect(true);
            }
            counterHandler.postDelayed(this, 1000);
        }
    };

}
