package com.tokopedia.affiliate.feature.createpost.domain.entity

import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentResponse

/**
 * @author by milhamj on 01/03/19.
 */
data class GetContentFormDomain(
        val feedContentResponse: FeedContentResponse? = FeedContentResponse(),
        val checkQuotaQuery: CheckQuotaQuery? = CheckQuotaQuery()
)