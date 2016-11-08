package com.tokopedia.core.purchase.presenter;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.purchase.activity.TxDetailActivity;
import com.tokopedia.core.purchase.model.response.txlist.OrderButton;
import com.tokopedia.core.purchase.model.response.txlist.OrderData;
import com.tokopedia.core.purchase.model.response.txlist.OrderShop;

/**
 * Created by Angga.Prasetiyo on 28/04/2016.
 */
public interface TxDetailPresenter {
    void processInvoice(TxDetailActivity txDetailActivity, OrderData data);

    void processToShop(Context context, OrderShop orderShop);

    void processShowComplain(Context context, OrderButton orderButton);

    void processOpenDispute(Context context, OrderData orderData, int state);

    void processConfirmDeliver(Context context, OrderData orderData);

    void processTrackOrder(Context context, OrderData orderData);

    void processUploadProof(Context context, OrderData orderData);

    void processSeeAllHistories(Context context, OrderData orderData);

    void processAskSeller(Context context, OrderData orderData);

    void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

    void onDestroyView();

}
