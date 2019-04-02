package com.tokopedia.train.search.data.specification;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.Map;

/**
 * Created by nabillasabbaha on 06/06/18.
 */
public class TrainAvailabilitySearchSpecification implements GqlNetworkSpecification {

    private Map<String, Object> mapParam;

    public TrainAvailabilitySearchSpecification(Map<String, Object> mapParam) {
        this.mapParam = mapParam;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_availability_query;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return mapParam;
    }
}
