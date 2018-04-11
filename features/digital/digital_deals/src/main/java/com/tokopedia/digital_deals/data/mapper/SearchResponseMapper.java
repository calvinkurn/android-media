package com.tokopedia.digital_deals.data.mapper;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import rx.functions.Func1;

public class SearchResponseMapper implements Func1<SearchResponse, SearchDomainModel> {
    @Override
    public SearchDomainModel call(SearchResponse searchResponseEntity) {
        CommonUtils.dumper("inside SearchResponseEntity = " + searchResponseEntity);
        return DealsSearchMapper.getSearchMapper().convertToSearchDomain(searchResponseEntity);
    }
}
