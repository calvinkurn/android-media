package com.tokopedia.events.domain;

import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by pranaymohapatra on 30/01/18.
 */

public class GetSearchNextUseCase extends UseCase<SearchDomainModel> {

    private final EventRepository eventRepository;

    public GetSearchNextUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<SearchDomainModel> createObservable(RequestParams requestParams) {
        String nextUrl = requestParams.getString("nexturl", "");
        return eventRepository.getSearchNext(nextUrl);
    }
}
