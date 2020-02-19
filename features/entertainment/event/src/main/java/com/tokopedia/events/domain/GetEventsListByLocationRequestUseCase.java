package com.tokopedia.events.domain;

import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class GetEventsListByLocationRequestUseCase extends UseCase<List<EventsCategoryDomain>> {
    public final String LOCATION = "location";
    private final EventRepository eventRepository;

    @Inject
    public GetEventsListByLocationRequestUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<List<EventsCategoryDomain>> createObservable(RequestParams requestParams) {
        return eventRepository.getEventsListByLocation(requestParams.getString(LOCATION, ""));

    }
}
