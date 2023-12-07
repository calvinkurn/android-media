package com.tokopedia.tokopedianow.home.domain.repository

import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartGqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.tokopedianow.test.R as tokopedianowtestR

object CartGraphqlResponse {

    val getMiniCartV3Response = GqlMockUtil
        .createSuccessResponse<MiniCartGqlResponse>(tokopedianowtestR.raw.get_mini_cart_v3_response)

    val addToCartResponse = GqlMockUtil
        .createSuccessResponse<AddToCartGqlResponse>(tokopedianowtestR.raw.add_to_cart_v2_response)
}
