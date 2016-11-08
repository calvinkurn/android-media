package com.tokopedia.core.purchase.interactor;

import android.content.Context;

import com.tokopedia.core.purchase.model.response.txverification.TxVerData;

/**
 * TxUploadInteractor
 * Created by anggaprasetiyo on 8/11/16.
 */
public interface TxUploadInteractor {

    void uploadImageProof(Context context, String imagePath, TxVerData txVerData, OnImageProofUpload listener);

    void unSubscribeObservable();

    interface OnImageProofUpload {

        void onSuccess(String message);

        void onFailed(String message);
    }
}
