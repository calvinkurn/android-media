package com.tokopedia.tkpd.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.tkpd.R;

/**
 * Created by Kris on 6/25/2015.
 */
public class FragmentVeritransView extends Fragment {
    public interface OnRedirectListener{
        void onRedirect(boolean success);
    }
    private class VtWebViewClient extends WebViewClient {

        boolean redirect = false;

        @Override
        public void onPageFinished(WebView view, String url) {
//            view.loadUrl("javascript:HTMLOUT.showHTML(document.documentElement.outerHTML);"); JANGAN DI DELETE untuk referensi seandainya butuh!!
            if(url.contains("token") && url.contains("callback") && redirect){
                counterHandler.removeCallbacks(runnable);
                listener.onRedirect(true);
            }else if(url.contains("token") && url.contains("callback")){
                redirect = true;
                counterHandler.post(runnable);
            }
            dialog.dismiss();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(getActivity()!=null)
                dialog.showDialog();
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
            showSslErrorDialog();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_web_view, container, false);
        webView = (WebView) view.findViewById(R.id.view);
        clearWebViewHistory();
        return view;
    }

    public static class TheJavaScriptInterface
    {
        @JavascriptInterface
        public void showHTML(String html)
        {
            System.out.print("Magic : " + html); // JANGAN DI DELETE emang untuk print hasil HTML
        }
    }
    private Context context;
    private View view;
    private String url;
    private WebView webView;
    private OnRedirectListener listener;
    private TkpdProgressDialog dialog;
    private int timeoutCounter;
    private Handler counterHandler;

    public static FragmentVeritransView createInstance(Context context){
        FragmentVeritransView veritransView = new FragmentVeritransView();
        veritransView.context = context;
        veritransView.counterHandler = new Handler();
        return veritransView;
    }
    public void setListener(OnRedirectListener listener){
        this.listener = listener;
    }
    public void getURL(String Url){
        url = Url;
    }
    public void showFragment(){
        dialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new TheJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new VtWebViewClient());
        webView.loadUrl(url);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeoutCounter++;
            Toast.makeText(context, Integer.toString(timeoutCounter), Toast.LENGTH_SHORT);
            if(timeoutCounter == 10){
                dialog.dismiss();
                listener.onRedirect(true);
            }
            counterHandler.postDelayed(this, 1000);
        }
    };
    private void clearWebViewHistory() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }
    private void showSslErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getResources().getString(R.string.title_credit_card_failed));
        builder.setNeutralButton(context.getString(R.string.button_ok), neutralButtonListener());
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
    private DialogInterface.OnClickListener neutralButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("result", "Failed");
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        };
    }
}
