package com.tokopedia.digital_deals.data;


import com.tokopedia.digital_deals.data.entity.response.allbrandsresponse.AllBrandsResponse;
import com.tokopedia.digital_deals.data.entity.response.alllocationresponse.LocationResponse;
import com.tokopedia.digital_deals.data.entity.response.branddetailsresponse.BrandDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.categorydetailresponse.CategoryResponse;
import com.tokopedia.digital_deals.data.entity.response.dealdetailsresponse.DealDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;

import java.util.HashMap;

import rx.Observable;



public interface DealsDataStore {

    Observable<DealsResponse> getDeals(HashMap<String, Object> params);

    Observable<DealsResponse> getDeals(String nextUrl);

    Observable<SearchResponse> getSearchDeals(HashMap<String, Object> params);

    Observable<SearchResponse> getSearchNext(String nextUrl);

    Observable<BrandDetailsResponse> getBrandDetails(String url);

    Observable<DealDetailsResponse> getDealDetails(String url);

    Observable<LocationResponse> getLocations();

    Observable<CategoryResponse> getCategoryDetails(HashMap<String, Object> parameters);

    Observable<CategoryResponse> getCategoryDetails(String nexturl);

    Observable<AllBrandsResponse> getAllBrands(HashMap<String, Object> params);

    Observable<AllBrandsResponse> getAllBrandsNext(String nextUrl);

}
