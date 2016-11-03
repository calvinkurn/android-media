package com.tokopedia.tkpd.purchase.listener;

import com.tokopedia.tkpd.product.listener.ViewListener;
import com.tokopedia.tkpd.purchase.model.response.txverinvoice.Detail;

import java.util.List;

/**
 * TxVerDetailViewListener
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public interface TxVerDetailViewListener extends ViewListener {
    void renderInvoiceList(List<Detail> detail);

    void setResult(int resultCode);
}
