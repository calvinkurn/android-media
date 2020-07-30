package com.tokopedia.events.data;

import com.tokopedia.events.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;

import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by pranaymohapatra on 15/02/18.
 */

public class SearchResponseMapper implements Func1<SearchResponse, SearchDomainModel> {
    @Override
    public SearchDomainModel call(SearchResponse searchResponseEntity) {
        Timber.d("inside SearchResponseEntity = " + searchResponseEntity);
        return EventSearchMapper.getSearchMapper().convertToSearchDomain(searchResponseEntity);
    }
}
