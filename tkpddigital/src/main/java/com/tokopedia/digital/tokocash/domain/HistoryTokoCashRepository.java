package com.tokopedia.digital.tokocash.domain;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.tokocash.network.apiservice.HistoryTokoCashService;
import com.tokopedia.digital.tokocash.entity.TokoCashHistoryEntity;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public class HistoryTokoCashRepository implements IHistoryTokoCashRepository {

    private final HistoryTokoCashService historyTokoCashService;

    public HistoryTokoCashRepository(HistoryTokoCashService historyTokoCashService) {
        this.historyTokoCashService = historyTokoCashService;
    }

    @Override
    public Observable<TokoCashHistoryEntity> getTokoCashHistoryData(String type, String startDate,
                                                                    String endDate, String afterId) {
        return historyTokoCashService.getApi().getHistoryTokocash(type, startDate, endDate, afterId)
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<TokoCashHistoryEntity>>() {
                    @Override
                    public Observable<TokoCashHistoryEntity> call(Response<TkpdDigitalResponse> response) {
                        return Observable
                                .just(response.body().convertDataObj(TokoCashHistoryEntity.class));
                    }
                });
    }
}
