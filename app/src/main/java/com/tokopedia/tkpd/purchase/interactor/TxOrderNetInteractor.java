package com.tokopedia.tkpd.purchase.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.purchase.model.ConfirmationData;
import com.tokopedia.tkpd.purchase.model.response.cancelform.CancelFormData;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.FormConfPaymentData;
import com.tokopedia.tkpd.purchase.model.response.formconfirmpayment.FormEditPaymentData;
import com.tokopedia.tkpd.purchase.model.response.txconfirmation.TxConfListData;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderListData;
import com.tokopedia.tkpd.purchase.model.response.txverification.TxVerListData;
import com.tokopedia.tkpd.purchase.model.response.txverinvoice.TxVerInvoiceData;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Angga.Prasetiyo on 11/04/2016.
 */
public interface TxOrderNetInteractor {

    interface TypeRequest {
        int INITIAL = 0;
        int LOAD_MORE = 1;
        int PULL_REFRESH = 2;
    }

    void getCancelPaymentForm(Context context, Map<String, String> params, OnCancelPaymentForm listener);

    void cancelPayment(Context context, Map<String, String> params, OnCancelPayment listener);

    void getOrderStatusList(@NonNull Context context, @NonNull Map<String, String> params,
                            @NonNull OnGetOrderStatusList listener);

    void getOrderReceiveList(@NonNull Context context, @NonNull Map<String, String> params,
                             @NonNull OnGetOrderReceiveList listener);

    void getOrderTxList(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull OnGetOrderTxList listener);

    void confirmFinishDeliver(@NonNull Context context, @NonNull Map<String, String> params,
                              @NonNull OnConfirmFinishDeliver listener);

    void confirmDeliver(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull OnConfirmFinishDeliver listener);

    void getPaymentConfirmationList(@NonNull Context context, @NonNull Map<String, String> params,
                                    @NonNull OnGetPaymentConfirmationList listener);

    void getPaymentVerificationList(@NonNull Context context, @NonNull Map<String, String> params,
                                    @NonNull OnGetPaymentVerificationList listener);

    void deliverReject(@NonNull Context context, @NonNull Map<String, String> params,
                       @NonNull OnDeliverReject listener);

    void getInvoiceData(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull OnGetInvoiceData listener);

    void uploadValidProofByPayment(Context context, Map<String, String> params,
                                   OnUploadProof listener);

    void confirmPayment(@NonNull Context context, @NonNull Map<String, String> params,
                        PaymentActionListener listener);

    void editPayment(@NonNull Context context, @NonNull Map<String, String> params,
                     PaymentActionListener listener);

    void getConfirmPaymentForm(@NonNull Context context, @NonNull Map<String, String> params,
                               ConfirmPaymentFormListener listener);

    void getEditPaymentForm(@NonNull Context context, @NonNull Map<String, String> params,
                            EditPaymentFormListener listener);

    void unSubscribeObservable();

    interface PaymentActionListener {

        void onSuccess(ConfirmationData data);

        void onError(String message);

        void onTimeout(String message);

        void onNoConnection(String message);

    }

    interface OnGetOrderStatusList {
        void onSuccess(JSONObject data, OrderListData dataObj);

        void onError(String message);

        void onEmptyData();
    }

    interface OnGetOrderReceiveList {
        void onSuccess(JSONObject data, OrderListData dataObj);

        void onError(String message);

        void onEmptyData();
    }

    interface OnGetOrderTxList {
        void onSuccess(JSONObject data, OrderListData dataObj);

        void onError(String message);

        void onEmptyData();
    }

    interface OnConfirmFinishDeliver {
        void onSuccess(String message, JSONObject lucky);

        void onError(String message);
    }

    interface OnGetPaymentConfirmationList {
        void onSuccess(JSONObject data, TxConfListData dataObj);

        void onError(String message);

        void onEmptyData();

        void onTimeout(String message);
    }


    interface OnDeliverReject {
        void onSuccess(String message);

        void onFailed(String message);

        void onError(String message);
    }

    interface OnCancelPaymentForm {

        void onSuccess(CancelFormData data);

        void onError(String message);
    }

    interface OnCancelPayment {

        void onSuccess();

        void onError(String message);
    }

    interface OnGetPaymentVerificationList {

        void onSuccess(TxVerListData data);

        void onError(String message);

        void onEmptyData();
    }

    interface OnGetInvoiceData {

        void onSuccess(TxVerInvoiceData data);

        void onError(String message);
    }

    interface OnUploadProof {
        void onSuccess(String message);

        void onFailed(String message);
    }

    interface ConfirmPaymentFormListener {
        void onSuccess(FormConfPaymentData data);

        void onError(String message);

        void onTimeout(String message);

        void onNoConnection(String message);
    }

    interface EditPaymentFormListener {
        void onSuccess(FormEditPaymentData data);

        void onError(String message);

        void onTimeout(String message);

        void onNoConnection(String message);
    }


}
