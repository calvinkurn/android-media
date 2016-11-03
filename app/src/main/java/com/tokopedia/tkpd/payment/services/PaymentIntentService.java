package com.tokopedia.tkpd.payment.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tokopedia.tkpd.payment.interactor.PaymentNetInteractor;
import com.tokopedia.tkpd.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.tkpd.payment.model.ParamParcel;
import com.tokopedia.tkpd.payment.model.responsecartstep1.CarStep1Data;
import com.tokopedia.tkpd.payment.model.responsedynamicpayment.DynamicPaymentData;

import java.util.Map;

/**
 * @author by Angga.Prasetiyo on 20/05/2016.
 */
public class PaymentIntentService extends IntentService {
    private static final String TAG = PaymentIntentService.class.getSimpleName();
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_PARAM_GET_PARAMETER_DYNAMIC_PAYMENT
            = "EXTRA_PARAM_GET_PARAMETER_DYNAMIC_PAYMENT";
    public static final String EXTRA_PARAM_STEP_1_PAYMENT = "EXTRA_PARAM_STEP_1_PAYMENT";
    public static final String EXTRA_RESULT_PARAM_GET_PARAMETER_DYNAMIC_PAYMENT
            = "EXTRA_RESULT_PARAM_GET_PARAMETER_DYNAMIC_PAYMENT";
    public static final String EXTRA_RESULT_STEP_1_PAYMENT = "EXTRA_RESULT_STEP_1_PAYMENT";
    public static final String EXTRA_RESULT_MESSAGE = "EXTRA_RESULT_MESSAGE";
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";

    public static final int RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_SUCCESS = 1;
    public static final int RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_ERROR = 2;
    public static final int RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_RUNNING = 3;
    public static final int RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_NO_CONNECTION = 4;

    public static final int RESULT_STEP_1_PAYMENT_SUCCESS = 5;
    public static final int RESULT_STEP_1_PAYMENT_ERROR = 6;
    public static final int RESULT_STEP_1_PAYMENT_RUNNING = 7;
    public static final int RESULT_STEP_1_NO_CONNECTION = 8;

    public static final int ACTION_GET_PARAMETER_DYNAMIC_PAYMENT = 1;
    public static final int ACTION_STEP_1_PAYMENT = 2;

    private PaymentNetInteractor paymentNetInteractor;

    public PaymentIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (intent.getIntExtra(EXTRA_ACTION, 0)) {
            case ACTION_GET_PARAMETER_DYNAMIC_PAYMENT:
                if (paymentNetInteractor == null)
                    paymentNetInteractor = new PaymentNetInteractorImpl();
                getParameterDynamicPayment(intent);
                break;
            case ACTION_STEP_1_PAYMENT:
                if (paymentNetInteractor == null)
                    paymentNetInteractor = new PaymentNetInteractorImpl();
                postPaymentStep1(intent);
        }
    }

    private void postPaymentStep1(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        final Map<String, String> params = ((ParamParcel)
                intent.getParcelableExtra(EXTRA_PARAM_STEP_1_PAYMENT)).getMap();
        receiver.send(RESULT_STEP_1_PAYMENT_RUNNING, null);
        paymentNetInteractor.postStep1(getBaseContext(), params,
                new PaymentNetInteractor.OnPostStep1() {
                    @Override
                    public void onSuccess(CarStep1Data data) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EXTRA_RESULT_STEP_1_PAYMENT, data);
                        receiver.send(RESULT_STEP_1_PAYMENT_SUCCESS, bundle);
                        stopSelf();
                    }

                    @Override
                    public void onError(String message) {
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_RESULT_MESSAGE, message);
                        receiver.send(RESULT_STEP_1_PAYMENT_ERROR, bundle);
                        stopSelf();
                    }

                    @Override
                    public void onNoConnection() {
                        receiver.send(RESULT_STEP_1_NO_CONNECTION, null);
                        stopSelf();
                    }
                });
    }

    private void getParameterDynamicPayment(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        final Map<String, String> params = ((ParamParcel)
                intent.getParcelableExtra(EXTRA_PARAM_GET_PARAMETER_DYNAMIC_PAYMENT)).getMap();
        receiver.send(RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_RUNNING, null);
        paymentNetInteractor.getParameterDynamicPayment(getBaseContext(), params,
                new PaymentNetInteractor.OnGetParameterDynamicPayment() {
                    @Override
                    public void onSuccess(DynamicPaymentData data) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EXTRA_RESULT_PARAM_GET_PARAMETER_DYNAMIC_PAYMENT, data);
                        receiver.send(RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_SUCCESS, bundle);
                        stopSelf();
                    }

                    @Override
                    public void onError(String message) {
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_RESULT_MESSAGE, message);
                        receiver.send(RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_ERROR, bundle);
                        stopSelf();
                    }

                    @Override
                    public void onNoConnection() {
                        receiver.send(RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_NO_CONNECTION, null);
                        stopSelf();
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (paymentNetInteractor != null)
            paymentNetInteractor.unSubscribeObservable();
        paymentNetInteractor = null;
    }
}
