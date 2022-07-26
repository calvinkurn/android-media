package com.tokopedia.feed_shop.shop.domain

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel

/**
 * @author by yfsx on 08/05/19.
 */
data class DynamicFeedShopDomain (
        val dynamicFeedDomainModel: DynamicFeedDomainModel = DynamicFeedDomainModel(),
        val whitelistDomain: WhitelistDomain = WhitelistDomain()
)