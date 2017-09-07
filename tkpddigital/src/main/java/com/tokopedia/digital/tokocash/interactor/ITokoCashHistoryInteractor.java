package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public interface ITokoCashHistoryInteractor {

    void getHistoryTokoCash(Subscriber<TokoCashHistoryData> subscriber, String type,
                            String startDate, String endDate, String afterId);

    void onDestroy();
}
