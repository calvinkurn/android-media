package com.tokopedia.core.invoice.presenter;

import android.content.Context;

import com.tokopedia.core.invoice.model.InvoiceRenderParam;

/**
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public interface InvoiceRenderPresenter {

    void processGetRenderedInvoice(Context context, InvoiceRenderParam data);
}
