package com.tokopedia.shop.common.graphql.data.shoplocation.gql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.data.GraphQLResult;
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult;

import java.util.ArrayList;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopLocationQuery implements HasGraphQLResult<ArrayList<ShopLocationModel>>{
    @SerializedName("shopLocations")
    @Expose
    GraphQLResult<ArrayList<ShopLocationModel>> shopLocationResult;

    @Override
    public GraphQLResult<ArrayList<ShopLocationModel>> getResult() {
        return shopLocationResult;
    }
}
