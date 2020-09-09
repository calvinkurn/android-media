package com.tokopedia.affiliate.feature.explore.domain.usecase

import android.text.TextUtils
import com.tokopedia.affiliate.feature.explore.data.pojo.AutoCompleteQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by yfsx on 24/10/18.
 */
class AutoCompleteUseCase @Inject constructor() : GraphqlUseCase() {

    private val query = """
        query topadsExploreAutocom(${'$'}keyword: String!, ${'$'}limit: Int!){
          topadsExploreAutocom(keyword: ${'$'}keyword, limit: ${'$'}limit){
            match{
                text
                formatted
                }
          }
        }
    """

    fun getRequest(keyword: String?): GraphqlRequest {
        return GraphqlRequest(
                query,
                AutoCompleteQuery::class.java,
                getParam(keyword).parameters,
                false
        )
    }

    companion object {
        private const val PARAM_KEYWORD = "keyword"
        private const val PARAM_LIMIT = "limit"
        private const val COUNT_LIMIT = 3
        fun getParam(keyword: String?): RequestParams {
            val params = RequestParams.create()
            if (!TextUtils.isEmpty(keyword)) {
                params.putString(PARAM_KEYWORD, keyword)
                params.putInt(PARAM_LIMIT, COUNT_LIMIT)
            }
            return params
        }
    }

}