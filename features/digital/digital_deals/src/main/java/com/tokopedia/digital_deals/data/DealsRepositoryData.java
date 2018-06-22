package com.tokopedia.digital_deals.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.LikeUpdateResponse;
import com.tokopedia.digital_deals.data.mapper.AllBrandsMapper;
import com.tokopedia.digital_deals.data.mapper.BrandDetailsTransformMapper;
import com.tokopedia.digital_deals.data.mapper.DealDetailsTransformMapper;
import com.tokopedia.digital_deals.data.mapper.DealsCategoryDetailMapper;
import com.tokopedia.digital_deals.data.mapper.DealsLocationTransformMapper;
import com.tokopedia.digital_deals.data.mapper.DealsSearchMapper;
import com.tokopedia.digital_deals.data.mapper.DealsTransformMapper;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.LikeUpdateResultDomain;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomainmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationDomainModel;
import com.tokopedia.digital_deals.domain.model.request.likes.GetLikesDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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
        return dealsDataStoreFactory.createCloudDataStore().getSearchDeals(params).map(new DealsSearchMapper());
    }

    @Override
    public Observable<SearchDomainModel> getSearchNext(String nextUrl) {
        return dealsDataStoreFactory.createCloudDataStore().getSearchNext(nextUrl).map(new DealsSearchMapper());
    }

    @Override
    public Observable<BrandDetailsDomain> getBrandDetails(String url, HashMap<String, Object> params) {
        return dealsDataStoreFactory.createCloudDataStore().getBrandDetails(url, params).map(new BrandDetailsTransformMapper());
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
    public Observable<CategoryDetailsDomain> getCategoryDetails(String url, HashMap<String, Object> parameters) {
        return dealsDataStoreFactory.createCloudDataStore().getCategoryDetails(url, parameters).map(new DealsCategoryDetailMapper());
    }

    @Override
    public Observable<CategoryDetailsDomain> getCategoryDetails(String nexturl) {
        return dealsDataStoreFactory.createCloudDataStore().getCategoryDetails(nexturl).map(new DealsCategoryDetailMapper());
    }

    @Override
    public Observable<AllBrandsDomain> getAllBrands(HashMap<String, Object> parameters) {
        return dealsDataStoreFactory.createCloudDataStore().getAllBrands(parameters).map(new AllBrandsMapper());
    }

    @Override
    public Observable<AllBrandsDomain> getAllBrandsNext(String nextUrl) {
        return dealsDataStoreFactory.createCloudDataStore().getAllBrandsNext(nextUrl).map(new AllBrandsMapper());
    }

    @Override
    public Observable<LikeUpdateResultDomain> updateLikes(JsonObject requestBody) {
        return dealsDataStoreFactory
                .createCloudDataStore()
                .updateLikes(requestBody).map(new Func1<LikeUpdateResponse, LikeUpdateResultDomain>() {
                    @Override
                    public LikeUpdateResultDomain call(LikeUpdateResponse likeUpdateResponse) {
                        LikeUpdateResultDomain likeUpdateResultDomain = new LikeUpdateResultDomain();
                        likeUpdateResultDomain.setMessage(likeUpdateResponse.getMessage());
                        likeUpdateResultDomain.setStatus(likeUpdateResponse.getStatus());
                        likeUpdateResultDomain.setLiked(likeUpdateResponse.isLiked());
                        return likeUpdateResultDomain;
                    }
                });
    }

    @Override
    public Observable<GetLikesDomain> getLikes(String dealId) {
        return dealsDataStoreFactory
                .createCloudDataStore()
                .getLikes(dealId).map(new Func1<JsonArray, GetLikesDomain>() {
                    @Override
                    public GetLikesDomain call(JsonArray response) {

                        GetLikesDomain getLikesDomain = null;
                        if (response != null) {
                            for (JsonElement entry : response) {
                                getLikesDomain = new Gson().fromJson(entry.getAsJsonObject(), GetLikesDomain.class);
                                break;
                            }
                        }
                        return getLikesDomain;
                    }
                });
    }
}
