package com.tokopedia.promocheckout.detail.domain

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail

data class DetailCouponMarkeplaceModel(var dataPromoCheckoutDetail: DataPromoCheckoutDetail? = null,
                                       var dataVoucher: GraphqlResponse? = null)