package com.tokopedia.common.travel.data.specification;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;

/**
 * Created by nabillasabbaha on 09/11/18.
 */
public interface DbFlowSpecification extends Specification {

    ConditionGroup getCondition();
}
