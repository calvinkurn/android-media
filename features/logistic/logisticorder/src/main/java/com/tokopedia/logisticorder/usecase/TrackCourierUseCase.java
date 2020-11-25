package com.tokopedia.logisticorder.usecase;

import com.tokopedia.logisticorder.mapper.ITrackingPageMapper;
import com.tokopedia.logisticCommon.data.repository.ITrackingPageRepository;
import com.tokopedia.logisticorder.uimodel.TrackingUiModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackCourierUseCase extends UseCase<TrackingUiModel>{

    private ITrackingPageRepository repository;
    private ITrackingPageMapper mapper;

    @Inject
    public TrackCourierUseCase(ITrackingPageRepository repository, ITrackingPageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Observable<TrackingUiModel> createObservable(RequestParams requestParams) {
        Map<String, String> parameters = new HashMap<>(requestParams.getParamsAllValueInString());
        return repository.getRates(parameters)
                .map(trackingResponse -> mapper.trackingUiModel(trackingResponse));
    }
}
