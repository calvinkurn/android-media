package com.tokopedia.nps.data.repository;

import com.tokopedia.nps.data.mapper.FeedbackMapper;
import com.tokopedia.nps.domain.Feedback;
import com.tokopedia.nps.domain.repository.FeedbackRepository;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public class FeedbackDataRepository implements FeedbackRepository {

    private final FeedbackDataStoreFactory dataStoreFactory;

    @Inject
    public FeedbackDataRepository(FeedbackDataStoreFactory dataStoreFactory) {
        this.dataStoreFactory = dataStoreFactory;
    }

    @Override
    public Observable<Feedback> post(RequestParams params) {
        final FeedbackMapper mapper = new FeedbackMapper();
        return this.dataStoreFactory
                .createDataStore()
                .post(params)
                .map(mapper);
    }
}
