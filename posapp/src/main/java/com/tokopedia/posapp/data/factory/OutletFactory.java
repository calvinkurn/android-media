package com.tokopedia.posapp.data.factory;

import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.posapp.data.mapper.GetOutletMapper;
import com.tokopedia.posapp.data.source.cloud.OutletCloudSource;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletFactory {
    private PeopleService peopleService;
    private GetOutletMapper getOutletMapper;

    public OutletFactory(PeopleService peopleService, GetOutletMapper getOutletMapper) {
        this.peopleService = peopleService;
        this.getOutletMapper = getOutletMapper;
    }

    public OutletCloudSource getOutletFromCloud() {
        return new OutletCloudSource(peopleService, getOutletMapper);
    }
}
