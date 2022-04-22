package com.tokopedia.shopadmin.feature.invitationaccepted.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object GetArticleDetailQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "articleDetail"
    private const val SLUG_KEY = "slug"
    private const val SLUG_VALUE = "syarat-dan-ketentuan-admin"

    private val ARTICLE_DETAIL_QUERY = """
        query articleDetail(${'$'}slug: String!){
          articleDetail(slug: ${'$'}slug) {
            data {
              blog {
                title
                html_content
              }
            }
          }
        }
    """.trimIndent()

    fun createRequestParams(): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SLUG_KEY, SLUG_VALUE)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = ARTICLE_DETAIL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}