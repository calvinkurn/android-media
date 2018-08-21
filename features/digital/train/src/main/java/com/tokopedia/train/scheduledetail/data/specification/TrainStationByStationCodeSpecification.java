package com.tokopedia.train.scheduledetail.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.station.data.databasetable.TrainStationDb_Table;
import com.tokopedia.train.station.data.specification.TrainStationSpecification;

/**
 * Created by Rizky on 08/06/18.
 */
public class TrainStationByStationCodeSpecification extends TrainStationSpecification implements DbFlowSpecification {

    private final String stationCode;

    public TrainStationByStationCodeSpecification(String stationCode) {
        this.stationCode = stationCode;
    }

    @Override
    public ConditionGroup getCondition() {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.or(TrainStationDb_Table.station_code.eq(stationCode));
        return conditions;
    }

}
