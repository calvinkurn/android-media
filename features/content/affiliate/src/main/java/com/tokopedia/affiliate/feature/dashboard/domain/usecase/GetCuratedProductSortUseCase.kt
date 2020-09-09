package com.tokopedia.affiliate.feature.dashboard.domain.usecase

import com.tokopedia.affiliate.feature.dashboard.data.pojo.AffiliateProductSortQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-09.
 */
class GetCuratedProductSortUseCase @Inject constructor() : GraphqlUseCase() {

    private val query = """
        query affiliateSortOption() {
          affiliatedProductSort {
            sorts {
              sortVal
              text
            }
          }
        }
    """

    fun getRequest(): GraphqlRequest {
        return GraphqlRequest(
                query,
                AffiliateProductSortQuery::class.java,
                false
        )
    }
}