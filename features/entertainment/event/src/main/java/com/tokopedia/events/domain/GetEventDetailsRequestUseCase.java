package com.tokopedia.events.domain;

import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class GetEventDetailsRequestUseCase extends UseCase<EventDetailsDomain> {
    private final EventRepository eventRepository;

    @Inject
    public GetEventDetailsRequestUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<EventDetailsDomain> createObservable(RequestParams requestParams) {
        String url = requestParams.getString("detailsurl", null);
        String substr = url.substring(0, 4);
        if (!substr.equals("http"))
            url = EventsUrl.EVENT_DETAIL + url;
        return eventRepository.getEventDetails(url);
    }
}
