package com.tokopedia.train.search.data.specification;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;

import java.util.Map;

/**
 * Created by nabillasabbaha on 3/12/18.
 */
public class TrainScheduleSpecification implements GqlNetworkSpecification {

    private Map<String, Object> mapParam;
    private int scheduleVariant;

    public TrainScheduleSpecification(Map<String, Object> mapParam) {
        this.mapParam = mapParam;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_schedule_query;
    }

    @Override
    public Map<String, Object> mapVariable() {
        return mapParam;
    }
}
