package com.tokopedia.train.seat.data.specification;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.Map;

public class TrainSeatSpecification implements GqlNetworkSpecification {
    private Map<String, Object> params;

    public TrainSeatSpecification(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_get_seats_query;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return params;
    }
}
