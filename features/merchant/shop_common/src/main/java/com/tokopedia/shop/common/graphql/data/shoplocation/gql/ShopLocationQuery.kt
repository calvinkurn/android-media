package com.tokopedia.shop.common.graphql.data.shoplocation.gql

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.graphql.data.GraphQLResult
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult
import java.util.ArrayList

/**
 * Created by hendry on 08/08/18.
 */

data class ShopLocationQuery(@SerializedName("shopLocations")
                             @Expose
                             override val result: GraphQLResult<ArrayList<ShopLocationModel>>? = null) :
        HasGraphQLResult<ArrayList<ShopLocationModel>> {

}
