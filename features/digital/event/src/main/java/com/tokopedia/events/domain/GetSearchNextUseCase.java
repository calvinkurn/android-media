package com.tokopedia.events.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;

import rx.Observable;

/**
 * Created by pranaymohapatra on 30/01/18.
 */

public class GetSearchNextUseCase extends UseCase<SearchDomainModel> {

    private final EventRepository eventRepository;

    public GetSearchNextUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<SearchDomainModel> createObservable(RequestParams requestParams) {
        String nextUrl = requestParams.getString("nexturl", "");
        return eventRepository.getSearchNext(nextUrl);
    }
}
