package com.tokopedia.train.common.specification;

import java.util.List;

/**
 * Created by nabillasabbaha on 04/02/19.
 */
public interface RoomSpecification extends Specification {

    String query();

    List<Object> getArgs();
}
