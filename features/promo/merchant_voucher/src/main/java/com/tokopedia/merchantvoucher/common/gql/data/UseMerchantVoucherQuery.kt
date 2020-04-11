package com.tokopedia.merchantvoucher.common.gql.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLResult
import com.tokopedia.merchantvoucher.common.gql.domain.mapper.HasGraphQLResult

import java.util.ArrayList

/**
 * Created by hendry on 08/08/18.
 */

data class UseMerchantVoucherQuery(@SerializedName("useMerchantVoucher")
                                @Expose
                                var result: UseMerchantVoucherQueryResult? = null)


data class UseMerchantVoucherQueryResult(@SerializedName("result")
                                      @Expose
                                      var result: Boolean = false,
                                      @SerializedName("error_message_title")
                                      @Expose
                                      var errorMessageTitle: String? = null,
                                      @SerializedName("error_message")
                                      @Expose
                                      var errorMessage: String? = null)
