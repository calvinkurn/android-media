package com.tokopedia.train.station.data.specification;

import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.specification.DbFlowWithOrderSpecification;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.station.data.databasetable.TrainStationDb_Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrainStationSpecification implements GqlNetworkSpecification, DbFlowWithOrderSpecification {
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

    @Override
    public List<OrderBy> toOrder() {
        List<OrderBy> orderBies = new ArrayList<OrderBy>();
        orderBies.add(OrderBy.fromProperty(TrainStationDb_Table.station_display_name).ascending());
        return orderBies;
    }
}
