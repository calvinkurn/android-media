package com.tokopedia.train.common.specification;

import com.raizlabs.android.dbflow.sql.language.OrderBy;

import java.util.List;

/**
 * @author by alvarisi on 3/7/18.
 */

public interface DbFlowWithOrderSpecification extends Specification {
    List<OrderBy> toOrder();
}
