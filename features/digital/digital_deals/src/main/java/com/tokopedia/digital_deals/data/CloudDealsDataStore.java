package com.tokopedia.digital_deals.data;

import com.tokopedia.digital_deals.data.entity.response.DealsResponseEntity;
import com.tokopedia.digital_deals.data.source.DealsApi;
import java.util.HashMap;
import rx.Observable;

public class CloudDealsDataStore implements DealsDataStore {

    private final DealsApi dealsApi;

    public CloudDealsDataStore(DealsApi dealsApi){
        this.dealsApi =dealsApi;
    }

    @Override
    public Observable<DealsResponseEntity> getDeals(HashMap<String, Object> params) {
        return dealsApi.getDeals();
    }
}
