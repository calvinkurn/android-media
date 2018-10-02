package com.tokopedia.merchantvoucher.common.gql.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLResult
import com.tokopedia.merchantvoucher.common.gql.domain.mapper.HasGraphQLResult

import java.util.ArrayList

/**
 * Created by hendry on 08/08/18.
 */

data class MerchantVoucherQuery(@SerializedName("shopShowcases")
                            @Expose
                            override var result: GraphQLResult<ArrayList<MerchantVoucherModel>>? = null)
    : HasGraphQLResult<ArrayList<MerchantVoucherModel>> {

}
