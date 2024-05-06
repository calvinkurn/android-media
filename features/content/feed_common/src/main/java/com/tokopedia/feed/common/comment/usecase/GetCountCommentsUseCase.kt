package com.tokopedia.feed.common.comment.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feed.common.comment.model.CountComment
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
class GetCountCommentsUseCase @Inject constructor(
    private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<GetCountCommentsUseCase.Param, CountComment>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY
    override suspend fun execute(params: Param): CountComment {
        return repo.request(graphqlQuery(), params.convertToMap())
    }

    data class Param(
        @SerializedName("contentIDs")
        val sourceId: List<String>,
        @SerializedName("contentType")
        val sourceType: String,
    ) {
        fun convertToMap() = mapOf<String, Any>(
            PARAM_ID to sourceId,
            PARAM_CONTENT_TYPE to sourceType,
        )
    }

    companion object {
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_ID = "contentIDs"

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
