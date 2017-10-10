package com.tokopedia.posapp.view.presenter;

import android.net.Uri;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.payment.PaymentStateDomain;
import com.tokopedia.posapp.domain.usecase.CheckPaymentStateUseCase;
import com.tokopedia.posapp.view.OTP;
import com.tokopedia.posapp.view.viewmodel.otp.OTPData;
import com.tokopedia.posapp.view.viewmodel.otp.OTPDetailTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Subscriber;

/**
 * Created by okasurya on 10/5/17.
 */

public class OTPPresenter implements OTP.Presenter {
    public static final String UTF_8 = "UTF-8";
    public static final String PARAM_URL = "url";
    public static final String PARAM_METHOD = "method";
    public static final String PARAM_GATEWAY = "gateway";
    public static final String PARAM_FORM = "form";
    private OTP.View viewListener;
    private OTPData otpData;

    private CheckPaymentStateUseCase checkPaymentStateUseCase;

    public OTPPresenter(OTP.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void initializeData(String jsonData) {
        if(jsonData != null) {
            try {
                JSONObject response = new JSONObject(jsonData);
                if(!response.isNull("errors")) {
                    JSONArray errors = response.getJSONArray("errors");
                    List<String> errorList = new ArrayList<>();
                    for(int i = 0; i < errors.length(); i ++) {
                        JSONObject error = errors.getJSONObject(i);
                        errorList.add(error.getString("detail"));
                    }
                    viewListener.onLoadDataError(errorList);
                    return;
                }

                JSONObject data = response.getJSONObject("data");
                if(data != null
                        && !data.getString(PARAM_URL).isEmpty()) {
                    otpData = new OTPData();
                    otpData.setUrl(data.getString(PARAM_URL));
                    otpData.setMethod(data.getString(PARAM_METHOD));
                    otpData.setGateway(data.getString(PARAM_GATEWAY));
                    otpData.setParameters(getQueryParam(data.getJSONObject(PARAM_FORM)).getBytes(UTF_8));
                    otpData.setOtpDetailTransaction(getDetailTransaction(data.getJSONObject(PARAM_FORM)));

                    if (otpData.getMethod().equals("POST")) {
                        viewListener.postOTPWebview(otpData);
                    } else {
                        viewListener.getOTPWebview(otpData);
                    }
                } else {
                    viewListener.onLoadDataError(new Exception("Empty url"));
                }
            } catch (Exception e) {
                viewListener.onLoadDataError(e);
            }
        }
    }

    private OTPDetailTransaction getDetailTransaction(JSONObject jsonObject) {
        OTPDetailTransaction detailTransaction = new OTPDetailTransaction();
        jsonObject.has("");
        detailTransaction.setSignature("");
        detailTransaction.setTransactionId("");
        detailTransaction.setId("");
        return null;
    }

    @Override
    public void checkPaymentState(String url) {
        Uri uri = Uri.parse(url).buildUpon().build();
        String id = uri.getQueryParameter("id");

        checkPaymentStateUseCase.execute(RequestParams.create())
        .subscribe(new Subscriber<PaymentStateDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(PaymentStateDomain paymentStateDomain) {

            }
        });
    }

    private String getQueryParam(JSONObject form) throws JSONException, UnsupportedEncodingException {
        String queryParam = "";
        Iterator<String> keys = form.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if(form.get(key) instanceof String) {
                queryParam += key + "=" + URLEncoder.encode((String) form.get(key), UTF_8);
                if(keys.hasNext()) queryParam += "&";
            } else if(form.get(key) instanceof JSONArray) {
                JSONArray jsonArray = form.getJSONArray(key);
                queryParam += key + "=" + URLEncoder.encode(jsonArray.getString(0), UTF_8);
                if(keys.hasNext()) queryParam += "&";
            }
        }
        return queryParam;
    }
}
