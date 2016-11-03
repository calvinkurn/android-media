package com.tokopedia.tkpd.purchase.listener;

import android.content.Intent;

import com.tokopedia.tkpd.product.listener.ViewListener;

/**
 * TxConfViewListener
 * Created by Angga.Prasetiyo on 28/04/2016.
 */
public interface TxDetailViewListener extends ViewListener {

    void closeWithResult(int requestCode, Intent data);
}
