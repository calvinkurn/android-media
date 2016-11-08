package com.tokopedia.core.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.facade.FacadeCreditCard;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Kris on 6/23/2015.
 */
public class SprintAsiaWebView {
    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onLoadResource(WebView view, String url) {
            dialog.setCancelable(false);
            progress.showDialog();
            System.out.println("Magic onLoadResource " + url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dialog.setCancelable(true);
            if(url.contains("tx-payment-cc-bca.pl")){
                view.loadUrl("javascript:HTMLOUT.printHTML(document.documentElement.outerHTML);");// JANGAN DI DELETE untuk referensi seandainya butuh!!
            }
            progress.dismiss();
            System.out.println("Magic PAGE FINISH " + url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            System.out.println("Magic PAGE STARTED " + url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

    }

    private Context context;
    private String url;
    private WebView webView;
    private Dialog dialog;
    private JSONObject param;
    private List<NameValuePair> paramList = new ArrayList<>();
    private PasswordGenerator password;
    private EncoderDecoder encoder;
    private FacadeCreditCard.OnSprintAsiaFinish sprintAsiaListener;
    private TkpdProgressDialog progress;

    public SprintAsiaWebView(Context context, String url){
        this.context = context;
        this.url = url;
        initVar();
    }

    private void initVar(){
        param = new JSONObject();
        password = new PasswordGenerator(context);
        encoder = new EncoderDecoder();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sprint_asia_web_view);
        progress = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    public void addParam(String key, String value){
        try {
            param.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void commitWithListener(FacadeCreditCard.OnSprintAsiaFinish listener){
        sprintAsiaListener = listener;
        commit();
    }
    private void commit(){
        addParam("app_id", password.getAppId());
        addParam("lang", "id");
        if (SessionHandler.isV4Login(context)) addParam("user_id", SessionHandler.getLoginID(context));
//        showDialog(getSCIV());
        runnableHTTP();
    }
    private String getSCIV(){// TODO HERE
        String SCIV = "";
        String uniqueID = UUID.randomUUID().toString().replaceAll("-","");
//        String postRequestID = UUID.randomUUID().toString(); TODO ???????????????????
        String IV = uniqueID.substring(0, 16);
        if (password.getAppId() != null && password.getSignature() != null)
            addParam("token", password.generatePassword(IV.substring(8, 16)));
        String SC = encoder.Encrypt(param.toString(), IV);
        SCIV = "?sc=" + SC + "&iv=" + IV;
        paramList.add(new BasicNameValuePair("sc", SC));
        paramList.add(new BasicNameValuePair("iv", IV));
        System.out.println("Dekrip SCIV = " + SCIV);
        return SCIV;
    }
    private void showDialog(String html){
        System.out.println("Sending POST to " + url);
        progress.showDialog();
        webView = (WebView) dialog.findViewById(R.id.sprint_asia_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.addJavascriptInterface(new LocalJavaScriptInterface(), "HTMLOUT");
        webView.getSettings().setUseWideViewPort(false);
        webView.loadData(html, "text/html", "UTF-8");
        CommonUtils.dumper(html);
        dialog.show();
    }
    private void runnableHTTP(){
        task tas = new task();
        tas.execute();
    }
    private class task extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            System.out.println("Magic EXECUTE");
            return postHTTP();
            //return getHTTP();
        }

        protected void onPostExecute(String param){
            showDialog(param);
        }

    }
    private String postHTTP(){
        System.out.println("Magic POSTHTTP");
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        //TODO ini yang penting itu di sini
        getSCIV();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramList));
            HttpResponse response = httpClient.execute(httpPost);
            String html = EntityUtils.toString(response.getEntity());
            return html;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";

    }
    private DialogInterface.OnDismissListener paymentFailedNotificationDismiss(){
        return new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                sprintAsiaListener.onTransactionFailed();
            }
        };
    }

    private View.OnTouchListener paymentFailedNotificationTouch(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sprintAsiaListener.onTransactionFailed();
                return true;
            }
        };
    }
    private class LocalJavaScriptInterface{
        @JavascriptInterface
        public void printHTML(String html){
            progress.dismiss();
            if(html.contains("value=\"1\"")){
                CommonUtils.dumper(html);
                sprintAsiaListener.onTransactionSuccess();
            } else {
                CommonUtils.dumper("lololololol");
                dialog.setOnDismissListener(paymentFailedNotificationDismiss());
                webView.setOnTouchListener(paymentFailedNotificationTouch());
            }
        }
    }
}
