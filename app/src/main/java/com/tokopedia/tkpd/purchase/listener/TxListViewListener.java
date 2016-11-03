package com.tokopedia.tkpd.purchase.listener;

import com.tokopedia.tkpd.product.listener.ViewListener;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderData;

import java.util.List;

/**
 * TxListViewListener
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public interface TxListViewListener extends ViewListener {
    void renderDataList(List<OrderData> orderDataList, boolean hasNext, int typeRequest);

    void showFailedLoadMoreData(String message);

    void showFailedPullRefresh(String message);

    void showFailedResetData(String message);

    void showEmptyData(int typeRequest);

    void showProcessGetData(int typeRequest);

    void resetData();

    void showMessageResiNumberCopied(String message);
}
