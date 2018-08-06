package com.tokopedia.train.station.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.common.specification.DbFlowWithOrderSpecification;
import com.tokopedia.train.station.data.databasetable.TrainStationDb_Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 3/7/18.
 */

public class TrainPopularStationSpecification extends TrainStationSpecification implements DbFlowSpecification, DbFlowWithOrderSpecification{

    public TrainPopularStationSpecification() {
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainStationDb_Table.popularity_order.greaterThan(0));
        return conditions;
    }

    @Override
    public List<OrderBy> toOrder() {
        List<OrderBy> orderBies = new ArrayList<OrderBy>();
        orderBies.add(OrderBy.fromProperty(TrainStationDb_Table.popularity_order).ascending());
        return orderBies;
    }
}
