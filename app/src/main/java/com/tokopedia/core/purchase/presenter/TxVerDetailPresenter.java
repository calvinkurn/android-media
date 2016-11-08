package com.tokopedia.core.purchase.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.purchase.model.response.txverification.TxVerData;
import com.tokopedia.core.util.UploadImageReVamp;

/**
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public interface TxVerDetailPresenter {

    void getTxInvoiceData(Context context, TxVerData txVerData);

    void processEditPayment(Context context, TxVerData txVerData);

    int getTypePaymentMethod(TxVerData txVerData);

    void processUploadProof(Context context, UploadImageReVamp uploadImage, TxVerData txVerData);

    void onDestroyView();

    void uploadProofImageWSV4(Activity activity, String imagePath, TxVerData txVerData);
}
