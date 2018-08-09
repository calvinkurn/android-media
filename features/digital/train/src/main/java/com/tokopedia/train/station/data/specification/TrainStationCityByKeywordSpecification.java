package com.tokopedia.train.station.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.tokopedia.train.common.specification.DbFlowGroupBySpecification;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.station.data.databasetable.TrainStationDb_Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 3/12/18.
 */

public class TrainStationCityByKeywordSpecification extends TrainStationSpecification implements DbFlowSpecification, DbFlowGroupBySpecification {

    private String keyword;

    public TrainStationCityByKeywordSpecification(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public ConditionGroup getCondition() {
        String query = keyword + "%";
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.or(TrainStationDb_Table.city_name.like(query));
        return conditions;
    }

    @Override
    public IProperty[] getProperty() {
        IProperty[] properties = new IProperty[1];
        properties[0] = TrainStationDb_Table.city_name;
        return properties;
    }

    @Override
    public List<OrderBy> toOrder() {
        List<OrderBy> orderBies = new ArrayList<OrderBy>();
        orderBies.add(OrderBy.fromProperty(TrainStationDb_Table.city_name).ascending());
        return orderBies;
    }
}
