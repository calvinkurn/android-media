package com.tokopedia.events.domain;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.events.domain.model.EventLocationDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class GetEventsLocationListRequestUseCase extends UseCase<List<EventLocationDomain>> {
    private final EventRepository eventRepository;

    @Inject
    public GetEventsLocationListRequestUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<List<EventLocationDomain>> createObservable(RequestParams requestParams) {
        return eventRepository.getEventsLocationList(requestParams.getParameters());
    }
}
