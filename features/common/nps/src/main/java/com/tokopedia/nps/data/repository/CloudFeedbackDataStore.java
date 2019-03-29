package com.tokopedia.nps.data.repository;

import com.tokopedia.nps.data.mapper.FeedbackEntityMapper;
import com.tokopedia.nps.data.model.FeedbackEntity;
import com.tokopedia.nps.data.net.NpsService;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public class CloudFeedbackDataStore implements FeedbackDataStore {

    private NpsService service;
    private FeedbackEntityMapper mapper;

    public CloudFeedbackDataStore(NpsService service, FeedbackEntityMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public Observable<FeedbackEntity> post(RequestParams params) {
        return service.getApi().postFeedback(params.getParamsAllValueInString()).map(mapper);
    }
}
