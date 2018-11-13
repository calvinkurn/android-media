package com.tokopedia.events.domain;

import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class GetSearchEventsListRequestUseCase extends UseCase<SearchDomainModel> {
    private final EventRepository eventRepository;
    public final String TAG = "tags";
    public final String TIME = "time";
    public final String CATEGORY_ID = "child_category_ids";
    public final String START_DATE = "start_date";

    @Inject
    public GetSearchEventsListRequestUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<SearchDomainModel> createObservable(RequestParams requestParams) {
        return eventRepository.getSearchEvents(requestParams.getParameters());

    }
}
