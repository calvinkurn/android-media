package com.tokopedia.core.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.facade.FacadeCreditCard;
import com.tokopedia.core.payment.interactor.PaymentNetInteractor;
import com.tokopedia.core.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.core.util.EncoderDecoder;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdUrl;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Kris on 6/25/2015.
 */
public class FragmentSprintAsiaWebView extends Fragment {
    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onLoadResource(WebView view, String url) {
            System.out.println("Magic onLoadResource " + url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains("tx-payment-cc-bca.pl") || url.contains("tx-payment-cc-bca-installment")) {
                view.loadUrl("javascript:HTMLOUT.printHTML(document.documentElement.outerHTML);");// JANGAN DI DELETE untuk referensi seandainya butuh!!
            }
            System.out.println("Magic PAGE FINISH " + url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            System.out.println("Magic PAGE STARTED " + url);
            if (url.contains("<html>")) {
                progress.dismiss();
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            showSslErrorDialog();
        }
    }

    public static FragmentSprintAsiaWebView createInstance(Context context, String url, FragmentCreditCard.Model model, FacadeCreditCard facadeAction) {
        FragmentSprintAsiaWebView fragmentSprintAsia = new FragmentSprintAsiaWebView();
        fragmentSprintAsia.context = context;
        fragmentSprintAsia.url = url;
        fragmentSprintAsia.model = model;
        fragmentSprintAsia.facadeAction = facadeAction;
        return fragmentSprintAsia;
    }

    private Context context;
    private String url;
    private WebView webView;
    private View view;
    private Dialog dialog;
    private JSONObject param;
    private List<NameValuePair> paramList = new ArrayList<>();
    private PasswordGenerator password;
    private EncoderDecoder encoder;
    private TkpdProgressDialog progress;
    private FacadeCreditCard facadeAction;
    private PaymentNetInteractor netInteractor;
    private FragmentCreditCard.Model model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sprint_asia_web_view, container, false);
        //Snackbar.make(container, R.string.instruction_credit_card_waiting, Snackbar.LENGTH_INDEFINITE).show();
        webView = (WebView) view.findViewById(R.id.sprint_asia_view);
        netInteractor = new PaymentNetInteractorImpl();
        progress.showDialog();
        netInteractor.postSprintAsia(getActivity(), getParam(), new PaymentNetInteractor.OnSprintAsia() {
            @Override
            public void onSuccess(String htmlSource) {
                progress.dismiss();
                setDialog(htmlSource);
            }

            @Override
            public void onError(String message) {
                CommonUtils.UniversalToast(getActivity(), message);
                progress.dismiss();
            }

            @Override
            public void onTimeout(String message) {
                CommonUtils.UniversalToast(getActivity(), message);
                progress.dismiss();
            }
        });

//        facadeAction.actionSprintAsia(model, param);
//        progress.showDialog();
//        commitWithListener();
        return view;

    }

    private Map<String, String> getParam() {
        Map<String, String> param = new HashMap<>();
        param.put("address_street", model.ccModel.address);
        param.put("amount", model.paymentAmount);
        param.put("billingAddress", model.ccModel.address);
        param.put("billingCity", model.ccModel.city);
        param.put("billingCountry", "ID");
        param.put("billingEmail", model.email);
        param.put("billingName", model.ccModel.ccName);
        param.put("billingPhone", model.ccModel.phone);
        param.put("billingPostalCode", model.ccModel.postCode);
        param.put("billingState", model.ccModel.province);
        param.put("cardExpMonth", model.ccModel.ccMonth);
        param.put("cardExpYear", model.ccModel.ccYear);
        param.put("cardNo", model.ccModel.ccNumber);
        param.put("cardSecurity", model.ccModel.ccCVV);
        param.put("cardType", model.cardType);
        param.put("city", model.ccModel.city);
        param.put("credit_card_edit_flag", "0");
        param.put("credit_card_token", "");
        param.put("currency", "IDR");
        param.put("deliveryAddress", model.ccModel.address);
        param.put("deliveryCity", model.ccModel.city);
        param.put("deliveryCountry", "ID");
        param.put("deliveryName", model.ccModel.ccName);
        param.put("deliveryPostalCode", model.ccModel.postCode);
        param.put("deliveryState", model.ccModel.province);
        param.put("first_name", model.ccModel.forename);
        param.put("last_name", model.ccModel.surname);
        param.put("merchantTransactionID", model.transactionID);
        param.put("merchantTransactionNote", "");
        param.put("phone", model.ccModel.phone);
        param.put("postal_code", model.ccModel.postCode);
        param.put("refback", TkpdUrl.TRANSACTION + "?gateway=12");
        param.put("serviceVersion", "1.1");
        if (model.ccModel.installment) {
            param.put("siteID", "mTkpdandroid");
            param.put("gateway", "12");
        } else {
            param.put("siteID", "mTokopediaandroid");
            param.put("gateway", "8");
        }
        param.put("step", "2");
        param.put("transactionType", "SALE");
        param.put("lp_flag", "1");
        addParam("app_id", password.getAppId());
        addParam("lang", "id");
        return param;
    }

    private void initVar() {
        param = new JSONObject();
        password = new PasswordGenerator(context);
        encoder = new EncoderDecoder();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sprint_asia_web_view);
        progress = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    public void addParam(String key, String value) {
        try {
            param.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void commitWithListener() {
        commit();
    }

    private void commit() {
        addParam("app_id", password.getAppId());
        addParam("lang", "id");
        if (SessionHandler.isV4Login(context))
            addParam("user_id", SessionHandler.getLoginID(context));
//        showDialog(getSCIV());
        runnableHTTP();
    }

    private String getSCIV() {// TODO HERE
        String SCIV = "";
        String uniqueID = UUID.randomUUID().toString().replaceAll("-", "");
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

    private void setDialog(String html) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.addJavascriptInterface(new LocalJavaScriptInterface(), "HTMLOUT");
        webView.getSettings().setUseWideViewPort(false);
        webView.loadData(html, "text/html", "UTF-8");
        CommonUtils.dumper(html);
    }

    private void runnableHTTP() {
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

        protected void onPostExecute(String param) {
            setDialog(param);
        }

    }

    private String postHTTP() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    private View.OnTouchListener paymentFailedNotificationTouch() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CreditCardFailed();
                return true;
            }
        };
    }

    private class LocalJavaScriptInterface {
        @JavascriptInterface
        public void printHTML(String html) {
            progress.dismiss();
            if (html.contains("value=\"1\"")) {
                CommonUtils.dumper(html);
//                sprintAsiaListener.onTransactionSuccess();
                SuccessSprintAsia();
            } else {
                CommonUtils.dumper("lololololol");
                webView.setOnTouchListener(paymentFailedNotificationTouch());
            }
        }
    }

    private void SuccessSprintAsia() {
        progress.dismiss();
        Intent intent = new Intent();
        intent.putExtra("result", FragmentCreditCard.sprintAsiaMarker);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void CreditCardFailed() {
        progress.dismiss();
        Intent intent = new Intent();
        intent.putExtra("result", "Failed");
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void showSslErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getResources().getString(R.string.title_credit_card_failed));
        builder.setNeutralButton(context.getString(R.string.button_ok), neutralButtonListener());
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private DialogInterface.OnClickListener neutralButtonListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CreditCardFailed();
            }
        };
    }
}
