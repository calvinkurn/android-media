package com.tokopedia.tkpd.invoice.presenter;

import android.content.Context;

import com.tokopedia.tkpd.invoice.model.InvoiceRenderParam;

/**
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public interface InvoiceRenderPresenter {

    void processGetRenderedInvoice(Context context, InvoiceRenderParam data);
}
