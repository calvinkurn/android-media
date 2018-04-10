package com.tokopedia.digital_deals.data;

import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
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
}
