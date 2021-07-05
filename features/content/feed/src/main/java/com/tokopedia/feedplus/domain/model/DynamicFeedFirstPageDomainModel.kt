package com.tokopedia.feedplus.domain.model

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain

/**
 * @author by milhamj on 07/01/19.
 */
data class DynamicFeedFirstPageDomainModel(
        val dynamicFeedDomainModel: DynamicFeedDomainModel = DynamicFeedDomainModel(),
        val isInterestWhitelist: Boolean = false,
        val shouldOverwrite: Boolean = true
)