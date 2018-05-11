package com.tokopedia.digital_deals.data.mapper;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.domain.model.DealsDomain;

import rx.functions.Func1;

public class DealsTransformMapper implements Func1<DealsResponse, DealsDomain> {

    @Override
    public DealsDomain call(DealsResponse dealsResponse) {
        CommonUtils.dumper("inside DealsResponse = " + dealsResponse);
        DealsEntityMapper dealsEntityMapper = new DealsEntityMapper();

        return dealsEntityMapper.transform(dealsResponse);
    }
}
