package com.tokopedia.train.station.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.station.domain.model.TrainStation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author by alvarisi on 3/12/18.
 */

public class TrainGetStationCitiesByKeywordUseCase extends UseCase<List<TrainStation>> {
    private static final String PARAM_KEYWORD = "PARAM_KEYWORD";
    private TrainRepository trainRepository;

    public TrainGetStationCitiesByKeywordUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<TrainStation>> createObservable(RequestParams requestParams) {
        return this.trainRepository.getStationCitiesByKeyword(requestParams.getString(PARAM_KEYWORD, ""));
    }

    public RequestParams createRequest(String keyword) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_KEYWORD, keyword);
        return requestParams;
    }
}
