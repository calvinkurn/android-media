package com.tokopedia.tkpd.purchase.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.tkpd.purchase.model.AllTxFilter;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderData;

/**
 *
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public interface TxListPresenter {

    void getStatusOrderData(Context context, int page, int typeRequest);

    void getDeliverOrderData(Context context, int page, int typeRequest);

    void getAllOrderData(Context context, int page, AllTxFilter filter, int typeRequest);

    void processToDetailOrder(Context context, OrderData data, int typeInstance);

    void processToInvoice(Context context, OrderData data);

    void processRejectOrder(Context context, OrderData data);

    void processUploadTx(Context context, OrderData data);

    void processConfirmDeliver(Context context, OrderData data, int typeInstance);

    void processOpenDispute(Context activity, OrderData data, int state);

    void processTrackOrder(Context context, OrderData data);

    void processShowComplain(Context context, OrderData data);

    void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

    void onDestroyView();

}
