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

data class ShopEtalaseByShopQuery(@SerializedName("shopShowcasesByShopID")
                            @Expose
                            override var result: GraphQLResult<ArrayList<ShopEtalaseModel>>? = null)
    : HasGraphQLResult<ArrayList<ShopEtalaseModel>> {

}
