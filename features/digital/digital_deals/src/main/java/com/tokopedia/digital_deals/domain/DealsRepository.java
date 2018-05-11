package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.HashMap;

import rx.Observable;

public interface DealsRepository {

    Observable<DealsDomain> getDeals(HashMap<String, Object> parameters);

    Observable<SearchDomainModel> getSearchDeals(HashMap<String, Object> params);

    Observable<SearchDomainModel> getSearchNext(String nextUrl);

    Observable<BrandDetailsDomain> getBrandDetails(String url);

    Observable<DealsDetailsDomain> getDealDetails(String url);
}
