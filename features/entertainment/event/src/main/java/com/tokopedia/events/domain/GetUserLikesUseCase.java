package com.tokopedia.events.domain;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by pranaymohapatra on 24/04/18.
 */

public class GetUserLikesUseCase extends UseCase<List<Integer>> {

    private final EventRepository eventRepository;

    @Inject
    public GetUserLikesUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<List<Integer>> createObservable(RequestParams requestParams) {
        return eventRepository.getUserLikes();
    }
}
