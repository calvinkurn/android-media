package com.tokopedia.digital_deals.domain;

import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.domain.model.LikeUpdateResultDomain;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomainmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationDomainModel;
import com.tokopedia.digital_deals.domain.model.request.likes.GetLikesDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.HashMap;

import rx.Observable;

public interface DealsRepository {

    Observable<DealsDomain> getDeals(HashMap<String, Object> parameters);

    Observable<DealsDomain> getDeals(String nextUrl);

    Observable<SearchDomainModel> getSearchDeals(HashMap<String, Object> params);

    Observable<SearchDomainModel> getSearchNext(String nextUrl);

    Observable<BrandDetailsDomain> getBrandDetails(String url, HashMap<String, Object> params);

    Observable<DealsDetailsDomain> getDealDetails(String url);

    Observable<LocationDomainModel> getLocations();

    Observable<CategoryDetailsDomain> getCategoryDetails(String url, HashMap<String, Object> parameters);

    Observable<CategoryDetailsDomain> getCategoryDetails(String nexturl);

    Observable<AllBrandsDomain> getAllBrands(HashMap<String, Object> parameters);

    Observable<AllBrandsDomain> getAllBrandsNext(String nextUrl);

    Observable<LikeUpdateResultDomain> updateLikes(JsonObject requestBody);

    Observable<GetLikesDomain> getLikes(String dealId);

}
