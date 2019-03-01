package com.tokopedia.affiliate.feature.createpost.domain.entity

import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.ContentFormData

/**
 * @author by milhamj on 01/03/19.
 */
data class GetContentFormDomain(
        val contentFormData: ContentFormData? = ContentFormData(),
        val checkQuotaQuery: CheckQuotaQuery? = CheckQuotaQuery()
)