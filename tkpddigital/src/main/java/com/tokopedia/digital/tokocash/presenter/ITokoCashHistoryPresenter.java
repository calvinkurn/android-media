package com.tokopedia.digital.tokocash.presenter;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public interface ITokoCashHistoryPresenter {

    void getWaitingTransaction();

    void getInitHistoryTokoCash(String type, String startDate, String endDate);

    void getHistoryLoadMore(String type, String startDate, String endDate);
}
