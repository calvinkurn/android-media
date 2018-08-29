package com.tokopedia.shop.common.graphql.data.shopbasicdata.gql

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLSuccess
import com.tokopedia.shop.common.graphql.data.GraphQLSuccessMessage

/**
 * Created by hendry on 08/08/18.
 */

data class ShopBasicDataMutation(@SerializedName("updateShopInfo")
                                 @Expose
                                 override val graphQLSuccessMessage: GraphQLSuccessMessage? = null)
    : HasGraphQLSuccess {
}
