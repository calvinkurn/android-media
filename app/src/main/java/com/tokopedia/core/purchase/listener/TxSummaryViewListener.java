package com.tokopedia.core.purchase.listener;

import com.tokopedia.core.purchase.model.TxSummaryItem;

import java.util.List;

/**
 * TxSummaryViewListener
 * Created by Angga.Prasetiyo on 07/04/2016.
 */
public interface TxSummaryViewListener {
    void renderPurchaseSummary(List<TxSummaryItem> summaryItemList);
}
