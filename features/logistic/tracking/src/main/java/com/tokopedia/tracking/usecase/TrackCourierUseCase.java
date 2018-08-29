package com.tokopedia.tracking.usecase;

import com.tokopedia.tracking.repository.ITrackingPageRepository;
import com.tokopedia.tracking.viewmodel.TrackingViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackCourierUseCase extends UseCase<TrackingViewModel>{

    private ITrackingPageRepository repository;

    public TrackCourierUseCase(ITrackingPageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<TrackingViewModel> createObservable(RequestParams requestParams) {
        Map<String, String> parameters = new HashMap<>();
        parameters.putAll(requestParams.getParamsAllValueInString());
        return repository.getRates(parameters);
    }
}
