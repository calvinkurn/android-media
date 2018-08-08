package com.tokopedia.shop.common.graphql.domain.mapper;

import com.tokopedia.shop.common.graphql.model.GraphQLResult;

/**
 * Created by hendry on 08/08/18.
 */

public interface HasGraphQLResult<T> {
    GraphQLResult<T> getResult();
}
