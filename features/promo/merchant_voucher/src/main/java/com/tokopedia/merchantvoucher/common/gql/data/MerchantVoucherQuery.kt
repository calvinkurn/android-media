package com.tokopedia.merchantvoucher.common.gql.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLResult
import com.tokopedia.merchantvoucher.common.gql.domain.mapper.HasGraphQLResult

import java.util.ArrayList

/**
 * Created by hendry on 08/08/18.
 */

data class MerchantVoucherQuery(@SerializedName("getPublicMerchantVoucherList")
                                @Expose
                                var result: MerchantVoucherQueryResult? = null) {
}


data class MerchantVoucherQueryResult(@SerializedName("vouchers")
                                      @Expose
                                      var vouchers: ArrayList<MerchantVoucherModel>? = null,
                                      @SerializedName("error_message_title")
                                      @Expose
                                      var errorMessageTitle: String? = null,
                                      @SerializedName("error_message")
                                      @Expose
                                      var errorMessage: String? = null) {

}
