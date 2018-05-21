package com.tokopedia.digital_deals.data;

import com.tokopedia.digital_deals.data.mapper.AllBrandsMapper;
import com.tokopedia.digital_deals.data.mapper.BrandDetailsTransformMapper;
import com.tokopedia.digital_deals.data.mapper.DealDetailsTransformMapper;
import com.tokopedia.digital_deals.data.mapper.DealsCategoryDetailMapper;
import com.tokopedia.digital_deals.data.mapper.DealsLocationTransformMapper;
import com.tokopedia.digital_deals.data.mapper.DealsTransformMapper;
import com.tokopedia.digital_deals.data.mapper.SearchResponseMapper;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.HashMap;

import rx.Observable;

public class DealsRepositoryData implements DealsRepository {

    private DealsDataStoreFactory dealsDataStoreFactory;

    public DealsRepositoryData(DealsDataStoreFactory dealsDataStoreFactory) {
        this.dealsDataStoreFactory = dealsDataStoreFactory;

    }

    @Override
    public Observable<DealsDomain> getDeals(HashMap<String, Object> parameters) {
        return dealsDataStoreFactory
                .createCloudDataStore()
                .getDeals(parameters).map(new DealsTransformMapper());
    }

    @Override
    public Observable<DealsDomain> getDeals(String nextUrl) {
        return dealsDataStoreFactory
                .createCloudDataStore()
                .getDeals(nextUrl).map(new DealsTransformMapper());
    }

    @Override
    public Observable<SearchDomainModel> getSearchDeals(HashMap<String, Object> params) {
        return dealsDataStoreFactory.createCloudDataStore().getSearchDeals(params).map(new SearchResponseMapper());
    }

    @Override
    public Observable<SearchDomainModel> getSearchNext(String nextUrl) {
        return dealsDataStoreFactory.createCloudDataStore().getSearchNext(nextUrl).map(new SearchResponseMapper());
    }

    @Override
    public Observable<BrandDetailsDomain> getBrandDetails(String url) {
        return dealsDataStoreFactory.createCloudDataStore().getBrandDetails(url).map(new BrandDetailsTransformMapper());
    }

    @Override
    public Observable<DealsDetailsDomain> getDealDetails(String url) {
        return dealsDataStoreFactory.createCloudDataStore().getDealDetails(url).map(new DealDetailsTransformMapper());
    }

    @Override
    public Observable<LocationDomainModel> getLocations() {
        return dealsDataStoreFactory.createCloudDataStore().getLocations().map(new DealsLocationTransformMapper());
    }

    @Override
    public Observable<CategoryDetailsDomain> getCategoryDetails(HashMap<String, Object> parameters) {
        return dealsDataStoreFactory.createCloudDataStore().getCategoryDetails(parameters).map(new DealsCategoryDetailMapper());
    }

    @Override
    public Observable<CategoryDetailsDomain> getCategoryDetails(String nexturl) {
        return dealsDataStoreFactory.createCloudDataStore().getCategoryDetails(nexturl).map(new DealsCategoryDetailMapper());
    }

    @Override
    public Observable<AllBrandsDomain> getAllBrands(String categoryUrl) {
        return dealsDataStoreFactory.createCloudDataStore().getAllBrands(categoryUrl).map(new AllBrandsMapper());
    }

    @Override
    public Observable<AllBrandsDomain> getAllBrandsNext(String nextUrl) {
        return dealsDataStoreFactory.createCloudDataStore().getAllBrandsNext(nextUrl).map(new AllBrandsMapper());
    }
}
