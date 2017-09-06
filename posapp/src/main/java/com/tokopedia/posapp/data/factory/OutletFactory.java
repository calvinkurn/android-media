package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.source.cloud.api.OutletApi;
import com.tokopedia.posapp.data.mapper.GetOutletMapper;
import com.tokopedia.posapp.data.source.cloud.OutletCloudSource;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletFactory {
    private OutletApi peopleApi;
    private GetOutletMapper getOutletMapper;

    public OutletFactory(OutletApi peopleApi, GetOutletMapper getOutletMapper) {
        this.peopleApi = peopleApi;
        this.getOutletMapper = getOutletMapper;
    }

    public OutletCloudSource cloud() {
        return new OutletCloudSource(peopleApi, getOutletMapper);
    }
}
