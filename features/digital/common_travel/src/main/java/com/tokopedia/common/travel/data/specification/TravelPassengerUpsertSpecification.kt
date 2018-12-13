package com.tokopedia.common.travel.data.specification

import com.raizlabs.android.dbflow.sql.language.ConditionGroup
import com.tokopedia.common.travel.database.TravelPassengerDb_Table

/**
 * Created by nabillasabbaha on 12/11/18.
 */
class TravelPassengerUpsertSpecification(private val idPassengerPrevious: String) : DbFlowSpecification {

    override fun getCondition(): ConditionGroup {
        val conditions = ConditionGroup.clause()
        conditions.or(TravelPassengerDb_Table.idPassenger.eq(idPassengerPrevious))
        return conditions
    }
}
