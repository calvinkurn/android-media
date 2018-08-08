package com.tokopedia.shop.common.graphql.model.shoplocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.model.GraphQLResult;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult;

import java.util.ArrayList;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopLocationResponse implements HasGraphQLResult<ArrayList<ShopLocation>>{
    @SerializedName("shopLocations")
    @Expose
    GraphQLResult<ArrayList<ShopLocation>> shopLocationResult;

    @Override
    public GraphQLResult<ArrayList<ShopLocation>> getResult() {
        return shopLocationResult;
    }
}
