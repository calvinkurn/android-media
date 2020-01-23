package com.tokopedia.events.data;

import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.domain.model.EventsCategoryDomain;

import java.util.List;

import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by naveengoyal on 2/15/18.
 */

public class EventTransformMapper implements Func1<EventResponseEntity, List<EventsCategoryDomain>> {
    @Override
    public List<EventsCategoryDomain> call(EventResponseEntity eventResponseEntity) {
        Timber.d("inside EventResponseEntity = " + eventResponseEntity);
        EventEntityMaper eventEntityMaper = new EventEntityMaper();

        return eventEntityMaper.tranform(eventResponseEntity);
    }
}
