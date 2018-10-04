package com.tokopedia.train.seat.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.mapper.TrainWagonViewModelMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class TrainGetSeatsUseCase extends UseCase<List<TrainWagonViewModel>> {
    private static final String PARAM_DEPARTURE_TIME = "date";
    private static final String PARAM_ORIGIN = "origin";
    private static final String PARAM_DESTINATION = "destination";
    private static final String PARAM_TRAIN_NO = "trainNo";
    private static final String PARAM_TRAIN_SUBCLASS = "subclass";

    private TrainRepository trainRepository;
    private TrainWagonViewModelMapper trainWagonViewModelMapper;

    public TrainGetSeatsUseCase(TrainRepository trainRepository, TrainWagonViewModelMapper trainWagonViewModelMapper) {
        this.trainRepository = trainRepository;
        this.trainWagonViewModelMapper = trainWagonViewModelMapper;
    }

    @Override
    public Observable<List<TrainWagonViewModel>> createObservable(RequestParams requestParams) {
        return trainRepository.getSeat(requestParams.getParameters())
                .map(new Func1<List<TrainSeatMapEntity>, List<TrainWagonViewModel>>() {
                    @Override
                    public List<TrainWagonViewModel> call(List<TrainSeatMapEntity> trainSeatMapEntities) {
                        if (trainSeatMapEntities != null)
                            return trainWagonViewModelMapper.transform(trainSeatMapEntities.get(0).getWagons());
                        else
                            throw new RuntimeException("wagon empty");
                    }
                });
    }

    public RequestParams createRequestParam(String departureTime, String origin, String destination, String trainNo, String subClass) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_DEPARTURE_TIME, departureTime);
        requestParams.putString(PARAM_ORIGIN, origin);
        requestParams.putString(PARAM_DESTINATION, destination);
        requestParams.putString(PARAM_TRAIN_NO, trainNo);
        requestParams.putString(PARAM_TRAIN_SUBCLASS, subClass);
        return requestParams;
    }
}
