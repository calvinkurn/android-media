package com.tokopedia.digital_deals.data;

import com.tokopedia.digital_deals.data.source.DealsApi;


public class DealsDataStoreFactory {
    private final DealsApi dealsApi;

    public DealsDataStoreFactory(DealsApi dealsApi) {
        this.dealsApi = dealsApi;
    }

    public DealsDataStore createCloudDataStore() {
        return new CloudDealsDataStore(dealsApi);
    }
}
