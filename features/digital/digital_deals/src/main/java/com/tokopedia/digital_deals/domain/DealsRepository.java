package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.HashMap;

import rx.Observable;

public interface DealsRepository {

    Observable<DealsDomain> getDeals(HashMap<String, Object> parameters);

    Observable<DealsDomain> getDeals(String nextUrl);

    Observable<SearchDomainModel> getSearchDeals(HashMap<String, Object> params);

    Observable<SearchDomainModel> getSearchNext(String nextUrl);

    Observable<BrandDetailsDomain> getBrandDetails(String url);

    Observable<DealsDetailsDomain> getDealDetails(String url);

    Observable<LocationDomainModel> getLocations();

    Observable<CategoryDetailsDomain> getCategoryDetails(HashMap<String, Object> parameters);

    Observable<CategoryDetailsDomain> getCategoryDetails(String nexturl);

    Observable<AllBrandsDomain> getAllBrands(String categoryUrl);

    Observable<AllBrandsDomain> getAllBrandsNext(String nextUrl);

}
