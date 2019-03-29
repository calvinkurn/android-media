package com.tokopedia.oms.data;

import com.tokopedia.oms.data.source.OmsApi;

import javax.inject.Inject;


public class OmsDataStoreFactory {
    private final OmsApi omsApi;

    @Inject
    public OmsDataStoreFactory(OmsApi omsApi){
        this.omsApi =omsApi;
    }

    public OmsDataStore createCloudDataStore(){
        return new CloudOmsDataStore(omsApi);
    }
}
