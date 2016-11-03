package com.tokopedia.tkpd.invoice.interactor;

import android.content.Context;

import java.util.Map;

/**
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public interface InvoiceNetInteractor {

    void renderInvoice(Context context, Map<String, String> params, OnRenderInvoice listener);

    interface OnRenderInvoice {

        void onSuccess(String htmlSource);

        void onTimeout(String message);

        void onError(String error);

        void onNullData(String message);

        void onNoInternetConnection(String message);
    }
}
