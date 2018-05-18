package com.tokopedia.events.data;

import com.tokopedia.events.data.source.EventsApi;
import com.tokopedia.events.data.source.PaymentApi;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventsDataStoreFactory {
    private final EventsApi eventsApi;
    private final PaymentApi paymentApi;

    public EventsDataStoreFactory(EventsApi eventsApi, PaymentApi paymentApi) {
        this.eventsApi = eventsApi;
        this.paymentApi = paymentApi;
    }

    public EventDataStore createCloudDataStore() {
        return new CloudEventsDataStore(eventsApi, paymentApi);
    }
}
