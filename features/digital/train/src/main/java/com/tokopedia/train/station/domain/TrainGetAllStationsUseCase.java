package com.tokopedia.train.station.domain;

import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.common.specification.DbFlowWithOrderSpecification;
import com.tokopedia.train.station.domain.model.TrainStation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;


import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class TrainGetAllStationsUseCase extends UseCase<List<TrainStation>> {
    private TrainRepository trainRepository;

    public TrainGetAllStationsUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<TrainStation>> createObservable(RequestParams requestParams) {
        return trainRepository.getAllStations();
    }
}
