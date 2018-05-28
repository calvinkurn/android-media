package com.tokopedia.digital_deals.data;

import com.tokopedia.digital_deals.data.entity.response.allbrandsresponse.AllBrandsResponse;
import com.tokopedia.digital_deals.data.entity.response.alllocationresponse.LocationResponse;
import com.tokopedia.digital_deals.data.entity.response.branddetailsresponse.BrandDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.categorydetailresponse.CategoryResponse;
import com.tokopedia.digital_deals.data.entity.response.dealdetailsresponse.DealDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.digital_deals.data.source.DealsApi;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationDomainModel;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;

import java.util.HashMap;

import rx.Observable;

public class CloudDealsDataStore implements DealsDataStore {

    private final DealsApi dealsApi;

    public CloudDealsDataStore(DealsApi dealsApi) {
        this.dealsApi = dealsApi;
    }

    @Override
    public Observable<DealsResponse> getDeals(HashMap<String, Object> params) {
        return dealsApi.getDeals(String.valueOf(params.get("url")));
    }

    @Override
    public Observable<DealsResponse> getDeals(String nextUrl) {
        return dealsApi.getDealsNextPage(nextUrl);
    }

    @Override
    public Observable<SearchResponse> getSearchDeals(HashMap<String, Object> params) {
        return dealsApi.getSearchDeals(params);
    }

    @Override
    public Observable<SearchResponse> getSearchNext(String nextUrl) {
        return dealsApi.getSearchNext(nextUrl);
    }

    @Override
    public Observable<BrandDetailsResponse> getBrandDetails(String url) {
        return dealsApi.getBrandDetails(url);
    }

    @Override
    public Observable<DealDetailsResponse> getDealDetails(String url) {
        return dealsApi.getDealDetails(url);
    }

    @Override
    public Observable<LocationResponse> getLocations() {
        return dealsApi.getLocations();
    }

    @Override
    public Observable<CategoryResponse> getCategoryDetails(HashMap<String, Object> params) {
        return dealsApi.getCategoryDetails(String.valueOf(params.get(DealsHomePresenter.TAG)));
    }

    @Override
    public Observable<CategoryResponse> getCategoryDetails(String nexturl) {
        return dealsApi.getCategoryDetailsNext(nexturl);
    }

    @Override
    public Observable<AllBrandsResponse> getAllBrands(HashMap<String, Object> params) {
            return dealsApi.getAllBrandsByCategory(params);
    }

    @Override
    public Observable<AllBrandsResponse> getAllBrandsNext(String nextUrl) {
        return dealsApi.getAllBrandsNext(nextUrl);
    }


}
