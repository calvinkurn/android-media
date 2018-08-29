package com.tokopedia.shop.common.graphql.data.shoplocation.gql

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.graphql.data.GraphQLResult
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult

/**
 * Created by hendry on 08/08/18.
 */

data class ShopLocationQuery(@SerializedName("shopLocations")
                             @Expose
                             private val shopLocationResult: GraphQLResult<List<ShopLocationModel>>? = null) : HasGraphQLResult<List<ShopLocationModel>> {

    override fun getResult(): GraphQLResult<List<ShopLocationModel>>? {
        return shopLocationResult
    }
}
