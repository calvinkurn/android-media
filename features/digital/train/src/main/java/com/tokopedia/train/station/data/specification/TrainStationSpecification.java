package com.tokopedia.train.station.data.specification;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.Map;

public class TrainStationSpecification implements GqlNetworkSpecification {
    public TrainStationSpecification() {
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_station_query;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return null;
    }
}
