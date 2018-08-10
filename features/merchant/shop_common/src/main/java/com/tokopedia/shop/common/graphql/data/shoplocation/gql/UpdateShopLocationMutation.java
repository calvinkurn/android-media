package com.tokopedia.shop.common.graphql.data.shoplocation.gql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.data.GraphQLSuccessMessage;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLSuccess;

/**
 * Created by hendry on 08/08/18.
 */

public class UpdateShopLocationMutation implements HasGraphQLSuccess {
    @SerializedName("updateShopLocation")
    @Expose
    private GraphQLSuccessMessage graphQLSuccessMessage;

    public GraphQLSuccessMessage getGraphQLSuccessMessage() {
        return graphQLSuccessMessage;
    }
}
