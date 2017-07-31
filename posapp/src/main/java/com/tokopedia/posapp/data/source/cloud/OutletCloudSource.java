package com.tokopedia.posapp.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.posapp.data.mapper.GetOutletMapper;
import com.tokopedia.posapp.domain.model.outlet.OutletDomain;

import rx.Observable;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletCloudSource {
    private PeopleService peopleService;
    private GetOutletMapper getOutletMapper;

    public OutletCloudSource(PeopleService peopleService, GetOutletMapper getOutletMapper) {
        this.peopleService = peopleService;
        this.getOutletMapper = getOutletMapper;
    }

    public Observable<OutletDomain> getOutlet(RequestParams requestParams) {
        return peopleService.getApi()
                .getAddress(requestParams.getParamsAllValueInString())
                .map(getOutletMapper);
    }
}
