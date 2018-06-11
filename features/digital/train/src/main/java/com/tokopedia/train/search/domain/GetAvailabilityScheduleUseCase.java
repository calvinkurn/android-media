package com.tokopedia.train.search.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class GetAvailabilityScheduleUseCase extends UseCase<List<TrainScheduleViewModel>> {

    public static final String TRAIN_ID_KEY = "avbKeys";

    private TrainRepository trainRepository;

    private int scheduleVariant;

    public GetAvailabilityScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setScheduleVariant(int scheduleVariant) {
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> createObservable(RequestParams requestParams) {
        return trainRepository.getAvailabilitySchedule(requestParams.getParameters(), scheduleVariant);
    }
}
