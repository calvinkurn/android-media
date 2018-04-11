package com.tokopedia.digital_deals.data;


import com.tokopedia.digital_deals.data.entity.response.DealsResponseEntity;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.HashMap;

import rx.Observable;



public interface DealsDataStore {

    Observable<DealsResponseEntity> getDeals(HashMap<String, Object> params);

    Observable<SearchResponse> getSearchDeals(HashMap<String, Object> params);

    Observable<SearchResponse> getSearchNext(String nextUrl);

}
