package com.tokopedia.core.invoice.listener;

/**
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public interface InvoiceViewListener{

    void renderInvoiceWeb(String htmlSource);

    void showProgress();

    void dismissProgress();

    void renderInvoiceError(String message);
}
