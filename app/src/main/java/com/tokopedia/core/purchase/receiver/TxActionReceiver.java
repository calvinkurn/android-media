package com.tokopedia.core.purchase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.purchase.listener.ConfirmPaymentViewListener;
import com.tokopedia.core.purchase.model.ConfirmationData;
import com.tokopedia.core.purchase.services.TxActionIntentService;

/**
 * TxActionReceiver
 * Created by Angga.Prasetiyo on 04/07/2016.
 */
public class TxActionReceiver extends BroadcastReceiver {
    public static final String TX_ACTION_RECEIVER = TxActionReceiver.class.getName();
    private final ConfirmPaymentViewListener confirmPaymentLsitener;

    public TxActionReceiver(ConfirmPaymentViewListener viewListener) {
        this.confirmPaymentLsitener = viewListener;
    }

    public static TxActionReceiver instanceConfirmPayment(ConfirmPaymentViewListener viewListener) {
        return new TxActionReceiver(viewListener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getIntExtra(TxActionIntentService.EXTRA_RESULT, 0)) {
            case TxActionIntentService.RESULT_ACTION_RUNNING:
                confirmPaymentLsitener.showProgressLoading();
                break;
            case TxActionIntentService.RESULT_EDIT_PAYMENT_SUCCESS:
            case TxActionIntentService.RESULT_CONFIRM_PAYMENT_SUCCESS:
                TxListUIReceiver.sendBroadcastForceRefreshListData(context);
                confirmPaymentLsitener.hideProgressLoading();
                confirmPaymentLsitener.renderConfirmationSuccess((ConfirmationData)
                        intent.getParcelableExtra(
                                TxActionIntentService.EXTRA_RESULT_CONFIRM_PAYMENT));
                break;
            case TxActionIntentService.RESULT_CONFIRM_PAYMENT_ERROR:
            case TxActionIntentService.RESULT_EDIT_PAYMENT_ERROR:
                confirmPaymentLsitener.hideProgressLoading();
                confirmPaymentLsitener.renderConfirmationError(
                        intent.getStringExtra(TxActionIntentService.EXTRA_MESSAGE));
                break;

            case TxActionIntentService.RESULT_CONFIRM_TIMEOUT:
                confirmPaymentLsitener.hideProgressLoading();
                confirmPaymentLsitener.renderConfirmationTimeout(
                        intent.getStringExtra(TxActionIntentService.EXTRA_MESSAGE));
                break;
            case TxActionIntentService.RESULT_CONFIRM_NO_CONNECTION:
                confirmPaymentLsitener.hideProgressLoading();
                confirmPaymentLsitener.renderConfirmationNoConnection(
                        intent.getStringExtra(TxActionIntentService.EXTRA_MESSAGE));
                break;
            case TxActionIntentService.RESULT_EDIT_TIMEOUT:
                confirmPaymentLsitener.hideProgressLoading();
                confirmPaymentLsitener.renderEditTimeout(
                        intent.getStringExtra(TxActionIntentService.EXTRA_MESSAGE));
                break;
            case TxActionIntentService.RESULT_EDIT_NO_CONNECTION:
                confirmPaymentLsitener.hideProgressLoading();
                confirmPaymentLsitener.renderConfirmationNoConnection(
                        intent.getStringExtra(TxActionIntentService.EXTRA_MESSAGE));
                break;
        }
    }
}
