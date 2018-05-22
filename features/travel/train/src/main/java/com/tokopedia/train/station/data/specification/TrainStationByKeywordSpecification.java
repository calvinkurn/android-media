package com.tokopedia.train.station.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.station.data.databasetable.TrainStationDb_Table;

/**
 * @author by alvarisi on 3/7/18.
 */

public class TrainStationByKeywordSpecification implements DbFlowSpecification {

    private String keyword;

    public TrainStationByKeywordSpecification(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public ConditionGroup getCondition() {
        String query = "%" + keyword + "%";
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.or(TrainStationDb_Table.station_code.like(query));
        conditions.or(TrainStationDb_Table.station_name.like(query));
        conditions.or(TrainStationDb_Table.station_display_name.like(query));
        conditions.or(TrainStationDb_Table.city_name.like(query));
        return conditions;
    }
}
