package com.tokopedia.affiliate.feature.dashboard.domain.usecase

import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * @author by yfsx on 19/09/18.
 */
class GetCuratedProductListUseCase @Inject constructor() : GraphqlUseCase() {
    private val query = """
        query GetCuratedProductList(${'$'}type: Int, ${'$'}cursor: String,${'$'}sort: Int,${'$'}startDate: String,${'$'}endDate: String) {
          affiliatedProduct(type: ${'$'}type, cursor: ${'$'}cursor, sortBy: ${'$'}sort, startDate: ${'$'}startDate, endDate: ${'$'}endDate) {
            products {
              id
              image
              name
              isActive
              totalSold
              totalClick
              commission
              productCommission
              createPostAppLink
              productRating
              reviewCount
            }
            pagination {
              nextCursor
            }
            subtitles {
              key
              text
            }
          }
        }
    """

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getRequest(type: Int?, cursor: String?, sort: Int?, startDate: Date?, endDate: Date?): GraphqlRequest {
        return GraphqlRequest(query,
                DashboardQuery::class.java,
                getParam(type, cursor, sort, startDate, endDate).parameters,
                false)
    }

    private fun getParam(type: Int?, cursor: String?, sort: Int?, startDate: Date?, endDate: Date?): RequestParams {
        val params = RequestParams.create()
        params.putString(PARAM_CURSOR, cursor)
        if (type != null) params.putInt(PARAM_TYPE, type)
        if (sort != null) params.putInt(PARAM_SORT, sort)
        if (startDate != null) params.putString(PARAM_START_DATE, dateFormat.format(startDate))
        if (endDate != null) params.putString(PARAM_END_DATE, dateFormat.format(endDate))
        return params
    }

    companion object {
        private const val PARAM_CURSOR = "cursor"

        /**
         * Available type:
         * https://tokopedia.atlassian.net/wiki/spaces/CN/pages/544965123/GQL+Affiliate+Dashboard+-+AffiliatedProductList
         * 1 - Curated product from create post
         * 2 - Curated product from traffic
         * Leave it blank if you wanna show both data
         */
        private const val PARAM_START_DATE = "startDate"
        private const val PARAM_END_DATE = "endDate"
        private const val PARAM_TYPE = "type"
        private const val PARAM_SORT = "sort"
    }

}