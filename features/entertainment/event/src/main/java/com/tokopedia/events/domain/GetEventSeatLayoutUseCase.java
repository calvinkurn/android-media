package com.tokopedia.events.domain;

import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by naveengoyal on 1/25/18.
 */

public class GetEventSeatLayoutUseCase extends UseCase<List<SeatLayoutItem>> {

    private final EventRepository eventRepository;

    @Inject
    public GetEventSeatLayoutUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<List<SeatLayoutItem>> createObservable(RequestParams requestParams) {
        String url = requestParams.getString("seatlayouturl", null);
        return eventRepository.getEventSeatLayout(url);
    }

}
