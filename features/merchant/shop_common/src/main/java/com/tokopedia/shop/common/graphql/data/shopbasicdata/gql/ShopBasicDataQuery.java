package com.tokopedia.shop.common.graphql.data.shopbasicdata.gql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.data.GraphQLResult;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopBasicDataQuery implements HasGraphQLResult<ShopBasicDataModel>{
    @SerializedName("shopBasicData")
    @Expose
    GraphQLResult<ShopBasicDataModel> shopBasicDataResult;

    @Override
    public GraphQLResult<ShopBasicDataModel> getResult() {
        return shopBasicDataResult;
    }
}
