package com.tokopedia.tokopedianow.home.domain.repository

import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokopedianow.test.R as tokopedianowtestR

object HomeGraphqlResponse {

    val getTargetedTickerResponse = GqlMockUtil
        .createSuccessResponse<GetTargetedTickerResponse>(tokopedianowtestR.raw.get_targeted_ticker_empty_response)

    val dynamicChannelChipCarouselResponse = GqlMockUtil
        .createSuccessResponse<GetHomeLayoutResponse>(tokopedianowtestR.raw.get_home_channel_v2_chip_carousel_response)

    val dynamicChannelProductCarouselResponse = GqlMockUtil
        .createSuccessResponse<GetHomeLayoutResponse>(tokopedianowtestR.raw.get_home_channel_v2_product_carousel_response)

    val getStateChoosenAddressResponse = GqlMockUtil
        .createSuccessResponse<GetStateChosenAddressQglResponse>(tokopedianowtestR.raw.get_state_choosen_address_response)
}
