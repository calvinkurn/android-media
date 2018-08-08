package com.tokopedia.shop.common.graphql.model.shopbasicdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.model.GraphQLResult;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopBasicDataResponse implements HasGraphQLResult<ShopBasicData>{
    @SerializedName("shopBasicData")
    @Expose
    GraphQLResult<ShopBasicData> shopBasicDataResult;

    @Override
    public GraphQLResult<ShopBasicData> getResult() {
        return shopBasicDataResult;
    }
}
