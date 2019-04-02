package com.tokopedia.posapp.payment.otp.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.bank.domain.model.BankDomain;
import com.tokopedia.posapp.payment.PaymentConst;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.posapp.payment.otp.domain.usecase.CheckTransactionWithRetryUseCase;
import com.tokopedia.posapp.payment.otp.OTP;
import com.tokopedia.posapp.payment.otp.view.viewmodel.OTPData;
import com.tokopedia.posapp.payment.otp.view.viewmodel.OTPDetailTransaction;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by okasurya on 10/5/17.
 */

public class OTPPresenter implements OTP.Presenter {
    private static final String UTF_8 = "UTF-8";
    private static final String PARAM_URL = "url";
    private static final String PARAM_METHOD = "method";
    private static final String PARAM_GATEWAY = "gateway";
    private static final String PARAM_FORM = "form";
    private static final String POST_METHOD = "POST";
    private static final String PARAM_TRANSACTION_ID = "transaction_id";
    private static final String PARAM_ID = "id";
    private static final String PARAM_SIGNATURE = "signature";
    private static final String PARAM_MERCHANT_CODE = "merchant_code";
    private static final String PARAM_IP_ADDRESS = "ip_address";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_DETAIL = "detail";
    private static final String PARAM_ERRORS = "errors";
//    private static final String CREATE_ORDER_PARAMETER = "CREATE_ORDER_PARAMETER";
    private static final String EMPTY_URL = "Empty url";
//    private static final String CREDITCARD = "CREDITCARD";
//    private static final String INSTALLMENT = "INSTALLMENT";
    public static final String PARAM_BANK_NAME = "bank_name";
    public static final String PARAM_BANK_ID = "bank_id";
    public static final String PARAM_BANK_LOGO = "bank_logo";
    public static final String REDIRECT_URL = "redirect_url";

    private OTP.View viewListener;
    private OTPData otpData;
    private CheckTransactionWithRetryUseCase checkTransactionWithRetryUseCase;
    private Context context;
    private BankDomain bankDomain;
    private String transactionId;
    private UserSession userSession;

    @Inject
    public OTPPresenter(@ApplicationContext Context context,
                        CheckTransactionWithRetryUseCase checkTransactionWithRetryUseCase,
                        UserSession userSession) {
        this.checkTransactionWithRetryUseCase = checkTransactionWithRetryUseCase;
        this.context = context;
        this.userSession = userSession;
    }

    public void attachView(OTP.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void initializeData(String jsonData) {
        try {
            Log.d("o2o", jsonData);
            JSONObject response = new JSONObject(jsonData);
            if (!response.isNull(PARAM_ERRORS)) {
                viewListener.onLoadDataError(response.optJSONArray(PARAM_ERRORS)
                        .getJSONObject(0).optString(PARAM_DETAIL));
                return;
            }

            setTransactionId(response.getString("transaction_id"));

            bankDomain = getBankData(response);

            JSONObject data = response.getJSONObject(PARAM_DATA).getJSONObject(PARAM_DATA);
            if (data != null && !data.getString(PARAM_URL).isEmpty()) {
                otpData = new OTPData();
                otpData.setUrl(data.getString(PARAM_URL));
                otpData.setMethod(data.getString(PARAM_METHOD));
                otpData.setGateway(data.getString(PARAM_GATEWAY));
                otpData.setParameters(getQueryParam(data.getJSONObject(PARAM_FORM)).getBytes(UTF_8));
                otpData.setOtpDetailTransaction(getDetailTransaction(data.getJSONObject(PARAM_FORM)));
                otpData.setRedirectUrl(data.getString(REDIRECT_URL));

                if (otpData.getMethod().equals(POST_METHOD)) {
                    viewListener.postOTPWebview(otpData);
                } else {
                    viewListener.getOTPWebview(otpData);
                }
            } else {
                viewListener.onLoadDataError(EMPTY_URL);
            }
        } catch (Exception e) {
            viewListener.onLoadDataError(e.getMessage());
        }
    }

    @Override
    public boolean isPaymentProcessed(String url) {
        return otpData != null && url.contains(otpData.getRedirectUrl());
    }

    @Override
    public void checkTransaction() {
        if (otpData != null
                && otpData.getOtpDetailTransaction() != null
                && otpData.getOtpDetailTransaction().getTransactionId() != null) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(PaymentConst.Parameter.TRANSACTION_ID, otpData.getOtpDetailTransaction().getTransactionId());

            checkTransactionWithRetryUseCase.execute(requestParams, new Subscriber<PaymentStatusDomain>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    viewListener.onPaymentError(e);
                }

                @Override
                public void onNext(PaymentStatusDomain paymentStatusDomain) {
                    if(bankDomain != null) {
                        paymentStatusDomain.setBankId(bankDomain.getBankId());
                        paymentStatusDomain.setBankName(bankDomain.getBankName());
                        paymentStatusDomain.setBankLogo(bankDomain.getBankLogo());
                    }
                    paymentStatusDomain.setTransactionId(otpData.getOtpDetailTransaction().getTransactionId());
                    viewListener.onPaymentCompleted(paymentStatusDomain);
                }
            });
        } else {
            viewListener.onPaymentError(new Exception("Data error"));
        }
    }

    @Override
    public void setTransactionId(String transactionId) throws RuntimeException {
        if(transactionId != null && !transactionId.isEmpty()) {
            this.transactionId = transactionId;
        } else {
            throw new RuntimeException("Transaction Id not found");
        }
    }

    @Override
    public void detachView() {
        viewListener = null;
        checkTransactionWithRetryUseCase.unsubscribe();
    }

    private BankDomain getBankData(JSONObject response) {
        try {
            if(response.has(PARAM_BANK_ID) && !response.isNull(PARAM_BANK_ID)
                    && response.has(PARAM_BANK_NAME) && !response.isNull(PARAM_BANK_NAME)
                    && response.has(PARAM_BANK_LOGO) && !response.isNull(PARAM_BANK_LOGO)) {
                BankDomain bankDomain = new BankDomain();
                bankDomain.setBankId(response.getInt(PARAM_BANK_ID));
                bankDomain.setBankName(response.getString(PARAM_BANK_NAME));
                bankDomain.setBankLogo(response.getString(PARAM_BANK_LOGO));
                return bankDomain;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private OTPDetailTransaction getDetailTransaction(JSONObject form) throws JSONException {
        OTPDetailTransaction detailTransaction = new OTPDetailTransaction();
        if (form.has(PARAM_SIGNATURE)) {
            detailTransaction.setSignature(getFormItem(PARAM_SIGNATURE, form));
        }
        if (transactionId != null && !transactionId.isEmpty()) {
            detailTransaction.setTransactionId(transactionId);
        }
        if (form.has(PARAM_ID)) {
            detailTransaction.setId(getFormItem(PARAM_ID, form));
        }
        return detailTransaction;
    }

    private String getQueryParam(JSONObject form) throws JSONException, UnsupportedEncodingException {
        String queryParam = "";
        Iterator<String> keys = form.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            queryParam += key + "=" + URLEncoder.encode(getFormItem(key, form), UTF_8);
            if (keys.hasNext()) queryParam += "&";
        }
        return queryParam;
    }

    private String getFormItem(String key, JSONObject form) throws JSONException {
        if (form.get(key) instanceof String) {
            return form.getString(key);
        } else if (form.get(key) instanceof JSONArray) {
            JSONArray jsonArray = form.getJSONArray(key);
            return jsonArray.getString(0);
        }
        return "";
    }
}
