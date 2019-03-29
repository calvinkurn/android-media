package com.tokopedia.tracking.usecase;

import com.tokopedia.tracking.mapper.ITrackingPageMapper;
import com.tokopedia.logisticdata.data.repository.ITrackingPageRepository;
import com.tokopedia.tracking.viewmodel.TrackingViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackCourierUseCase extends UseCase<TrackingViewModel>{

    private ITrackingPageRepository repository;
    private ITrackingPageMapper mapper;

    @Inject
    public TrackCourierUseCase(ITrackingPageRepository repository, ITrackingPageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Observable<TrackingViewModel> createObservable(RequestParams requestParams) {
        Map<String, String> parameters = new HashMap<>(requestParams.getParamsAllValueInString());
        return repository.getRates(parameters)
                .map(trackingResponse -> mapper.trackingViewModel(trackingResponse));
    }
}
