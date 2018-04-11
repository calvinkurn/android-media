package com.tokopedia.digital_deals.data;

import com.tokopedia.digital_deals.data.mapper.DealsTransformMapper;
import com.tokopedia.digital_deals.data.mapper.SearchResponseMapper;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class DealsRepositoryData implements DealsRepository {

    private DealsDataStoreFactory dealsDataStoreFactory;

    public DealsRepositoryData(DealsDataStoreFactory dealsDataStoreFactory) {
        this.dealsDataStoreFactory = dealsDataStoreFactory;

    }

    @Override
    public Observable<List<DealsCategoryDomain>> getDeals(HashMap<String, Object> parameters) {
        return dealsDataStoreFactory
                .createCloudDataStore()
                .getDeals(parameters).map(new DealsTransformMapper());
    }

    @Override
    public Observable<SearchDomainModel> getSearchDeals(HashMap<String, Object> params) {
        return dealsDataStoreFactory.createCloudDataStore().getSearchDeals(params).map(new SearchResponseMapper());
    }

    @Override
    public Observable<SearchDomainModel> getSearchNext(String nextUrl) {
        return dealsDataStoreFactory.createCloudDataStore().getSearchNext(nextUrl).map(new SearchResponseMapper());
    }
}
