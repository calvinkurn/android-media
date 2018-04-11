package com.tokopedia.digital_deals.data.mapper;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.digital_deals.data.entity.response.DealsResponseEntity;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;

import java.util.List;

import rx.functions.Func1;

public class DealsTransformMapper implements Func1<DealsResponseEntity, List<DealsCategoryDomain>> {

    @Override
    public List<DealsCategoryDomain> call(DealsResponseEntity dealsResponseEntity) {
        CommonUtils.dumper("inside DealsResponseEntity = " + dealsResponseEntity);
        DealsEntityMapper dealsEntityMapper = new DealsEntityMapper();

        return dealsEntityMapper.transform(dealsResponseEntity);
    }
}
