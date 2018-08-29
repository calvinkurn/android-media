package com.tokopedia.shop.common.graphql.data.shopetalase.gql

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.graphql.data.GraphQLResult
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult

import java.util.ArrayList

/**
 * Created by hendry on 08/08/18.
 */

data class ShopEtalaseQuery(@SerializedName("shopShowcases")
                            @Expose
                            internal var shopEtalaseResult: GraphQLResult<ArrayList<ShopEtalaseModel>>? = null) : HasGraphQLResult<ArrayList<ShopEtalaseModel>> {

    override fun getResult(): GraphQLResult<ArrayList<ShopEtalaseModel>>? {
        return shopEtalaseResult
    }
}
