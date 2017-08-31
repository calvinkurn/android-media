package com.tokopedia.digital.tokocash.listener;

import com.tokopedia.digital.tokocash.model.HeaderHistory;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public interface TokoCashHistoryListener {

    void showLoading();

    void hideLoading();

    void showLoadingHistory();

    void hideLoadingHistory();

    void setHasNextPage(boolean hasNextPage);

    void renderDataTokoCashHistory(TokoCashHistoryData tokoCashHistoryData, boolean firstTimeLoad);

    void renderEmptyTokoCashHistory(List<HeaderHistory> headerHistoryList);

    void renderErrorMessage(Throwable throwable);
}
