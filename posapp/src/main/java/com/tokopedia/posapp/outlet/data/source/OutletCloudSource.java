package com.tokopedia.posapp.outlet.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.outlet.data.mapper.GetOutletMapper;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;

import rx.Observable;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletCloudSource {
    private OutletApi outletApi;
    private GetOutletMapper getOutletMapper;

    public OutletCloudSource(OutletApi outletApi, GetOutletMapper getOutletMapper) {
        this.outletApi = outletApi;
        this.getOutletMapper = getOutletMapper;
    }

    public Observable<OutletDomain> getOutlet(RequestParams params) {
        return outletApi.getAddress(params.getParamsAllValueInString()).map(getOutletMapper);
    }
}
