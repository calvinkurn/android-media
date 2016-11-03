package com.tokopedia.tkpd.purchase.presenter;

import android.content.Context;

import com.tokopedia.tkpd.purchase.model.response.txconfirmation.TxConfData;

import java.util.Set;

/**
 * Created by Angga.Prasetiyo on 13/05/2016.
 */
public interface TxConfirmationPresenter {
    void getPaymentConfirmationData(Context context, int page, int typeRequest);

    void processToTxConfirmationDetail(Context context, TxConfData data);

    void processMultiConfirmPayment(Context context, Set<TxConfData> datas);

    void processMultipleCancelPayment(Context context, Set<TxConfData> datas);

    void onDestroyView();

}
