package com.tokopedia.train.seat.data.specification;

import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.Map;

public class TrainSeatSpecification implements GqlNetworkSpecification {
    private Map<String, Object> params;

    public TrainSeatSpecification(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public int rawFileNameQuery() {
        return 0;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return params;
    }
}
