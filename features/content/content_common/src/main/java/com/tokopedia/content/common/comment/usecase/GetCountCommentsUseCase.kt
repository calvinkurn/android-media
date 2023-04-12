package com.tokopedia.content.common.comment.usecase

import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.model.CountComment
import com.tokopedia.content.common.producttag.domain.usecase.FeedAceSearchShopUseCase
import com.tokopedia.content.common.producttag.domain.usecase.GetShopInfoByIDUseCase
import com.tokopedia.content.common.producttag.domain.usecase.GetSortFilterUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
@GqlQuery(GetCountCommentsUseCase.QUERY_NAME, GetCountCommentsUseCase.QUERY)
class GetCountCommentsUseCase @Inject constructor(repo: GraphqlRepository) :
    GraphqlUseCase<CountComment>(repo) {

    init {
        setGraphqlQuery(GetCountCommentsUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(CountComment::class.java)
    }

    companion object {
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_ID = "contentIDs"

        fun setParam(
            source: PageSource,
        ) = mapOf(
            PARAM_ID to listOf(source.id),
            PARAM_CONTENT_TYPE to source.type,
        )

        const val QUERY_NAME = "GetCountCommentsUseCaseQuery"
        const val QUERY = """
           query getCountComment(${"$$PARAM_ID"}: [String!]!, ${"$$PARAM_CONTENT_TYPE"}: String!){
                  feedsCommentCountComments($PARAM_ID: ${"$$PARAM_ID"}, $PARAM_CONTENT_TYPE: ${"$$PARAM_CONTENT_TYPE"}) {
                        data {
                          countCommentsData {
                            contentID
                            countComments
                            countCommentsFmt
                            isShowComments
                          }
                        }
                    error
                  }
                }
           """
    }
}
