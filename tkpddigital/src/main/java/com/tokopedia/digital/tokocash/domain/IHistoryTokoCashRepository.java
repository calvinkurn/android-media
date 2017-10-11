package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.digital.tokocash.entity.TokoCashHistoryEntity;

import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface IHistoryTokoCashRepository {

    Observable<TokoCashHistoryEntity> getTokoCashHistoryData(String type, String startDate,
                                                             String endDate, String afterId);
}
