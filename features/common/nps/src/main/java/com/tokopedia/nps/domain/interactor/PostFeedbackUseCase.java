package com.tokopedia.nps.domain.interactor;

import com.tokopedia.nps.domain.Feedback;
import com.tokopedia.nps.domain.repository.FeedbackRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by meta on 28/06/18.
 */
public class PostFeedbackUseCase extends UseCase<Feedback> {

    private FeedbackRepository repository;

    @Inject
    public PostFeedbackUseCase(FeedbackRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Feedback> createObservable(RequestParams requestParams) {
        return this.repository.post(requestParams);
    }
}
