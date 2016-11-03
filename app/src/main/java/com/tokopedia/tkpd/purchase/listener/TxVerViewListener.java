package com.tokopedia.tkpd.purchase.listener;

import com.tokopedia.tkpd.product.listener.ViewListener;
import com.tokopedia.tkpd.purchase.model.response.txverification.TxVerData;

import java.util.List;

/**
 * TxVerViewListener
 * Created by Angga.Prasetiyo on 24/05/2016.
 */
public interface TxVerViewListener extends ViewListener {

    void renderDataList(List<TxVerData> orderDataList, boolean hasNext, int typeRequest);

    void showFailedLoadMoreData(String message);

    void showFailedPullRefresh(String message);

    void showFailedResetData(String message);

    void showEmptyData(int typeRequest);

    void showProcessGetData(int typeRequest);

    void resetData();
}
