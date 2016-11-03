package com.tokopedia.tkpd.purchase.services;

import android.app.IntentService;
import android.content.Intent;

import com.tokopedia.tkpd.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.tkpd.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.tkpd.purchase.model.ConfirmPaymentData;
import com.tokopedia.tkpd.purchase.model.ConfirmationData;
import com.tokopedia.tkpd.purchase.receiver.TxActionReceiver;
import com.tokopedia.tkpd.purchase.receiver.TxListUIReceiver;
import com.tokopedia.tkpd.purchase.utils.ParamUtil;

/**
 * TxActionIntentService
 * Created by Angga.Prasetiyo on 01/07/2016.
 */
public class TxActionIntentService extends IntentService {
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_DATA_CONFIRM = "EXTRA_DATA_CONFIRM";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String EXTRA_RESULT_CONFIRM_PAYMENT = "EXTRA_RESULT_CONFIRM_PAYMENT";

    public static final int ACTION_CONFIRM_PAYMENT = 1;
    public static final int ACTION_EDIT_PAYMENT = 2;
    public static final int ACTION_UNDEFINED = 0;

    public static final int RESULT_ACTION_RUNNING = 1;
    public static final int RESULT_CONFIRM_PAYMENT_ERROR = 2;
    public static final int RESULT_CONFIRM_PAYMENT_SUCCESS = 3;
    public static final int RESULT_CONFIRM_NO_CONNECTION = 6;
    public static final int RESULT_CONFIRM_TIMEOUT = 7;
    public static final int RESULT_EDIT_PAYMENT_ERROR = 4;
    public static final int RESULT_EDIT_PAYMENT_SUCCESS = 5;
    public static final int RESULT_EDIT_NO_CONNECTION = 8;
    public static final int RESULT_EDIT_TIMEOUT = 9;
    private TxOrderNetInteractor netInteractor;


    public TxActionIntentService() {
        super(TxActionIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (netInteractor == null) netInteractor = new TxOrderNetInteractorImpl();
        switch (intent.getIntExtra(EXTRA_ACTION, ACTION_UNDEFINED)) {
            case ACTION_CONFIRM_PAYMENT:
                actionConfirmPayment(intent);
                break;
            case ACTION_EDIT_PAYMENT:
                actionEditPayment(intent);
                break;
            default:
                undefinedAction();
                break;
        }
    }

    private void undefinedAction() {
        throw new RuntimeException("Action ga ada coy!!!");
    }

    private void actionConfirmPayment(Intent intent) {
        ConfirmPaymentData data = intent.getParcelableExtra(EXTRA_DATA_CONFIRM);
        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(TxActionReceiver.TX_ACTION_RECEIVER);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_ACTION_RUNNING);
        sendBroadcast(broadcastIntent);
        if (data.getBankAccountId().equals("ADD_NEW")) {
            data.setBankAccountId("");
        }
        netInteractor.confirmPayment(getBaseContext(), ParamUtil.generateParamConfirmPayment(data),
                new TxOrderNetInteractor.PaymentActionListener() {
                    @Override
                    public void onSuccess(ConfirmationData data) {
                        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_CONFIRM_PAYMENT_SUCCESS);
                        broadcastIntent.putExtra(EXTRA_RESULT_CONFIRM_PAYMENT, data);
                        sendBroadcast(broadcastIntent);
                        stopSelf();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_CONFIRM_PAYMENT_ERROR);
                        broadcastIntent.putExtra(EXTRA_MESSAGE, errorMsg);
                        sendBroadcast(broadcastIntent);
                        stopSelf();
                    }

                    @Override
                    public void onTimeout(String errorMsg) {
                        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_CONFIRM_TIMEOUT);
                        broadcastIntent.putExtra(EXTRA_MESSAGE, errorMsg);
                        sendBroadcast(broadcastIntent);
                        stopSelf();
                    }

                    @Override
                    public void onNoConnection(String message) {
                        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_CONFIRM_NO_CONNECTION);
                        sendBroadcast(broadcastIntent);
                        stopSelf();
                    }
                });
    }

    private void actionEditPayment(Intent intent) {
        ConfirmPaymentData data = intent.getParcelableExtra(EXTRA_DATA_CONFIRM);
        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(TxActionReceiver.TX_ACTION_RECEIVER);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_ACTION_RUNNING);
        sendBroadcast(broadcastIntent);
        netInteractor.editPayment(getBaseContext(), ParamUtil.generateParamEditPayment(data),
                new TxOrderNetInteractor.PaymentActionListener() {
                    @Override
                    public void onSuccess(ConfirmationData data) {
                        TxListUIReceiver.sendBroadcastForceRefreshListData(TxActionIntentService.this);
                        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_EDIT_PAYMENT_SUCCESS);
                        broadcastIntent.putExtra(EXTRA_RESULT_CONFIRM_PAYMENT, data);
                        sendBroadcast(broadcastIntent);
                        stopSelf();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_EDIT_PAYMENT_ERROR);
                        broadcastIntent.putExtra(EXTRA_MESSAGE, errorMsg);
                        sendBroadcast(broadcastIntent);
                        stopSelf();
                    }

                    @Override
                    public void onTimeout(String errorMsg) {
                        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_EDIT_TIMEOUT);
                        broadcastIntent.putExtra(EXTRA_MESSAGE, errorMsg);
                        sendBroadcast(broadcastIntent);
                        stopSelf();
                    }

                    @Override
                    public void onNoConnection(String message) {
                        broadcastIntent.putExtra(EXTRA_RESULT, RESULT_EDIT_NO_CONNECTION);
                        sendBroadcast(broadcastIntent);
                        stopSelf();
                    }
                });
    }
}
