package com.tokopedia.shop.common.graphql.model.shopetalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.model.GraphQLResult;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult;

import java.util.ArrayList;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopEtalaseResponse implements HasGraphQLResult<ArrayList<ShopEtalase>>{
    @SerializedName("shopShowcases")
    @Expose
    GraphQLResult<ArrayList<ShopEtalase>> shopEtalaseResult;

    @Override
    public GraphQLResult<ArrayList<ShopEtalase>> getResult() {
        return shopEtalaseResult;
    }
}
