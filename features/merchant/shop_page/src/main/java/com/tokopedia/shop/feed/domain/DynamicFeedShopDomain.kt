package com.tokopedia.shop.feed.domain

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel

/**
 * @author by yfsx on 08/05/19.
 */
data class DynamicFeedShopDomain (
        val dynamicFeedDomainModel: DynamicFeedDomainModel = DynamicFeedDomainModel(),
        val whitelistDomain: WhitelistDomain = WhitelistDomain()
)