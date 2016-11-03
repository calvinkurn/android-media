package com.tokopedia.tkpd.invoice.listener;

import com.tokopedia.tkpd.product.listener.ViewListener;

/**
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public interface InvoiceViewListener{

    void renderInvoiceWeb(String htmlSource);

    void showProgress();

    void dismissProgress();

    void renderInvoiceError(String message);
}
