package com.tokopedia.events.data;

import com.tokopedia.events.data.entity.response.EventsDetailsEntity;
import com.tokopedia.events.domain.model.EventDetailsDomain;

import rx.functions.Func1;

/**
 * Created by pranaymohapatra on 15/02/18.
 */

public class EventDetailsResponseMapper implements Func1<EventsDetailsEntity, EventDetailsDomain> {
    @Override
    public EventDetailsDomain call(EventsDetailsEntity eventsDetailsEntity) {
        EventEntityMaper eventEntityMaper = new EventEntityMaper();
        return eventEntityMaper.tranformEventDetails(eventsDetailsEntity);
    }
}
