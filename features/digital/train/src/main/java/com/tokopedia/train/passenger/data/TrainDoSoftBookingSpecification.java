package com.tokopedia.train.passenger.data;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.Map;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainDoSoftBookingSpecification implements GqlNetworkSpecification {

    private Map<String, Object> mapParam;

    public TrainDoSoftBookingSpecification(Map<String, Object> mapParam) {
        this.mapParam = mapParam;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_softbooking_mutation;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return mapParam;
    }
}
