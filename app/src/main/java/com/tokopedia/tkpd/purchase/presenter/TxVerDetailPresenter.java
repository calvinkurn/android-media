package com.tokopedia.tkpd.purchase.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.tkpd.purchase.activity.TxVerDetailActivity;
import com.tokopedia.tkpd.purchase.model.response.txverification.TxVerData;
import com.tokopedia.tkpd.util.UploadImageReVamp;

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
