package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

public interface DealsRepository {

    Observable<List<DealsCategoryDomain>> getDeals(HashMap<String, Object> parameters);

    Observable<SearchDomainModel> getSearchDeals(HashMap<String, Object> params);

    Observable<SearchDomainModel> getSearchNext(String nextUrl);
}
