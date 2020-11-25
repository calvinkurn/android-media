package com.tokopedia.affiliate.common.domain.usecase

import com.tokopedia.affiliatecommon.analytics.CheckQuotaQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import javax.inject.Inject

/**
 * @author by yfsx on 12/10/18.
 */
class CheckQuotaUseCase @Inject constructor() : GraphqlUseCase() {

    private val query = """
        query {
            affiliatePostQuota() {
                formatted
                number
                format
            }
        }
    """

    val request: GraphqlRequest
        get() = GraphqlRequest(
                query,
                CheckQuotaQuery::class.java, false
        )

}