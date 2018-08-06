package com.tokopedia.train.search.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class GetDetailScheduleUseCase extends UseCase<TrainScheduleViewModel> {

    private TrainRepository trainRepository;
    private String idSchedule;

    public GetDetailScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setIdSchedule(String idSchedule) {
        this.idSchedule = idSchedule;
    }

    @Override
    public Observable<TrainScheduleViewModel> createObservable(RequestParams requestParams) {
        return trainRepository.getDetailSchedule(idSchedule);
    }
}
