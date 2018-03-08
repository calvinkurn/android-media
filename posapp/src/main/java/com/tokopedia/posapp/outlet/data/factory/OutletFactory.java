package com.tokopedia.posapp.outlet.data.factory;

import com.tokopedia.posapp.outlet.data.source.OutletApi;
import com.tokopedia.posapp.outlet.data.mapper.GetOutletMapper;
import com.tokopedia.posapp.outlet.data.source.OutletCloudSource;

/**
 * Created by okasurya on 7/31/17.
 * Deprecated because of unnecessary layer
 */
@Deprecated
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
