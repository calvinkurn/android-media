package com.tokopedia.tkpd.purchase.listener;

import com.tokopedia.tkpd.product.listener.ViewListener;
import com.tokopedia.tkpd.purchase.model.response.txconfirmation.TxConfData;

import java.util.List;

/**
 * TxConfViewListener
 * Created by Angga.Prasetiyo on 13/05/2016.
 */
public interface TxConfViewListener extends ViewListener {
    void renderDataList(List<TxConfData> orderDataList, boolean hasNext, int typeRequest);

    void showFailedLoadMoreData(String message);

    void showFailedPullRefresh(String message);

    void showFailedResetData(String message);

    void showEmptyData(int typeRequest);

    void showProcessGetData(int typeRequest);

    void resetData();

    void closeActionMode();

    void resetTxConfDataSelected();
}
