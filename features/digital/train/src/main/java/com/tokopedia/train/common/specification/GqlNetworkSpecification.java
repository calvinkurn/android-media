package com.tokopedia.train.common.specification;

import java.util.Map;

/**
 * @author by alvarisi on 3/7/18.
 */

public interface GqlNetworkSpecification extends Specification {
    int rawFileNameQuery();

    Map<String, Object> mapVariable();
}
