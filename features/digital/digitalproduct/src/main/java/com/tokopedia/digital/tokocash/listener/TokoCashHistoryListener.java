package com.tokopedia.digital.tokocash.listener;

import com.tokopedia.digital.tokocash.model.HeaderHistory;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public interface TokoCashHistoryListener {

    void hideLoading();

    void setHasNextPage(boolean hasNextPage);

    void renderDataTokoCashHistory(TokoCashHistoryData tokoCashHistoryData, boolean firstTimeLoad);

    void renderEmptyTokoCashHistory(List<HeaderHistory> headerHistoryList);

    void renderErrorMessage(String message);

    void renderEmptyPage(String message);

    void renderWaitingTransaction(TokoCashHistoryData tokoCashHistoryData);

    void hideWaitingTransaction();
}
