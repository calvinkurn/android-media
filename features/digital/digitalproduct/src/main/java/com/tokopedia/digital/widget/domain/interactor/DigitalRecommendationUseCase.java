package com.tokopedia.digital.widget.domain.interactor;

import com.tokopedia.digital.widget.data.repository.DigitalWidgetRepository;
import com.tokopedia.digital.widget.view.model.Recommendation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by Rizky on 14/11/18.
 */
public class DigitalRecommendationUseCase extends UseCase<List<Recommendation>> {

    private static final String PARAM_REQUEST_ID = "PARAM_REQUEST_ID";

    private DigitalWidgetRepository digitalWidgetRepository;

    public DigitalRecommendationUseCase(DigitalWidgetRepository digitalWidgetRepository) {
        this.digitalWidgetRepository = digitalWidgetRepository;
    }

    @Override
    public Observable<List<Recommendation>> createObservable(RequestParams requestParams) {
        int deviceId = requestParams.getInt(PARAM_REQUEST_ID, 0);

        return digitalWidgetRepository.getRecommendationList(deviceId);
    }

    public RequestParams createRequestParams(Integer deviceId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_REQUEST_ID, deviceId);
        return requestParams;
    }
}
