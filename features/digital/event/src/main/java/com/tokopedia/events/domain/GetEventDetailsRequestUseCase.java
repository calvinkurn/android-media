package com.tokopedia.events.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.events.domain.model.EventDetailsDomain;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class GetEventDetailsRequestUseCase extends UseCase<EventDetailsDomain> {
    private final EventRepository eventRepository;

    public GetEventDetailsRequestUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
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
