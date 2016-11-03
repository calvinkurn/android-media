package com.tokopedia.tkpd.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Entity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.facade.FacadeCreditCard;
import com.tokopedia.tkpd.fragment.FragmentCart;
import com.tokopedia.tkpd.fragment.FragmentCartSummary;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Tkpd_Eka on 5/28/2015.
 */
public class WebViewHandler {

    private class CustomWebViewClient extends WebViewClient{

        @Override
        public void onLoadResource(WebView view, String url) {
            System.out.println("Magic onLoadResource " + url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(url.contains("tx-payment-cc-bca.pl")){
                view.loadUrl("javascript:HTMLOUT.printHTML(document.documentElement.outerHTML);");// JANGAN DI DELETE untuk referensi seandainya butuh!!
            }

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

    public WebViewHandler(Context context, String url){
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
        dialog.setContentView(R.layout.dialog_web_view);
    }

    public void addParam(String key, String value){
        try {
            param.put(key, value);
            paramList.add(new BasicNameValuePair(key, value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void commitWithListener(FacadeCreditCard.OnSprintAsiaFinish listener){
        sprintAsiaListener = listener;
        commit();
    }
    public void commit(){
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
        webView = (WebView) dialog.findViewById(R.id.view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.addJavascriptInterface(new LocalJavaScriptInterface(), "HTMLOUT");
        webView.getSettings().setUseWideViewPort(false);
//        webView.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));
        webView.loadData(html, "text/html", "UTF-8");
        CommonUtils.dumper(html);
        dialog.show();
    }

    private void runnableHTTP(){
//        Thread benang = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                postHTTP();
//            }
//        });
//        benang.start();
        task tas = new task();
        tas.execute();
    }

    private class task extends AsyncTask<Void, Void, String>{


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
//        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//        httpPost.setHeader("Referer", context.getString(R.string.tx_payment_sprintasia));
        //TODO ini yang penting itu di sini
            getSCIV();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramList));
            HttpResponse response = httpClient.execute(httpPost);
            String html = EntityUtils.toString(response.getEntity());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//            StringBuilder builder = new StringBuilder();
//            for(String line=null; (line = reader.readLine()) != null;){
//                builder.append(line).append("\n");
//            }
//            String html = builder.toString();
            return html;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";

    }
    private String getHTTP(){
        HttpClient httpClient = new DefaultHttpClient();
        String SCIV = getSCIV();
        SCIV = SCIV.replaceAll("\n", "");
        String params = URLEncodedUtils.format(paramList, "utf-8");
        String uri = url+SCIV;
        CommonUtils.dumper(uri);
        try {
            URI Uri = new URI(uri);
            HttpGet httpGet = new HttpGet(Uri);
            HttpResponse response = httpClient.execute(httpGet);
            String html = EntityUtils.toString(response.getEntity());
            return html;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //httpGet.setParams(SCIV);
        return "";
    }
    private class LocalJavaScriptInterface{
        @JavascriptInterface
        public void printHTML(String html){
            if(html.contains("value=\"1\"")){
                CommonUtils.dumper(html);
                sprintAsiaListener.onTransactionSuccess();
            }else{
                CommonUtils.dumper("lololololol");
                sprintAsiaListener.onTransactionFailed();
            }
        }
    }
}
