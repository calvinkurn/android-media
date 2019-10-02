package com.tokopedia.createpost.domain.entity

import com.tokopedia.affiliatecommon.analytics.CheckQuotaQuery
import com.tokopedia.createpost.data.pojo.getcontentform.FeedContentResponse


/**
 * @author by milhamj on 01/03/19.
 */
data class GetContentFormDomain(
        val feedContentResponse: FeedContentResponse? = FeedContentResponse(),
        val checkQuotaQuery: CheckQuotaQuery? = CheckQuotaQuery()
)