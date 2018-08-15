package com.tokopedia.shop.common.graphql.data.shoplocation.gql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.data.GraphQLResult;
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult;

import java.util.List;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopLocationQuery implements HasGraphQLResult<List<ShopLocationModel>>{
    @SerializedName("shopLocations")
    @Expose
    private GraphQLResult<List<ShopLocationModel>> shopLocationResult;

    @Override
    public GraphQLResult<List<ShopLocationModel>> getResult() {
        return shopLocationResult;
    }
}
