package com.tokopedia.train.seat.data.specification;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainChangeSeatSpecification implements GqlNetworkSpecification {
    private List<ChangeSeatMapRequest> requests;

    public TrainChangeSeatSpecification(List<ChangeSeatMapRequest> requests) {
        this.requests = requests;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_get_seats_query;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return new HashMap<>();
    }
}