package com.tokopedia.kolcommon.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kolcommon.data.pojo.like.LikeKolPostData
import javax.inject.Inject

/**
 * Created by meyta.taliti on 04/08/22.
 */
@GqlQuery(SubmitLikeContentUseCase.QUERY_NAME, SubmitLikeContentUseCase.QUERY)
class SubmitLikeContentUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<LikeKolPostData>(graphqlRepository) {

    init {
        setGraphqlQuery(SubmitLikeContentUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(LikeKolPostData::class.java)
    }

    companion object {
        private const val PARAM_ID_POST = "idPost"
        private const val PARAM_ACTION = "action"

        const val ACTION_LIKE = "1"
        const val ACTION_UNLIKE = "0"

        const val SUCCESS = 1

        const val QUERY_NAME = "SubmitLikeContentUseCaseQuery"
        const val QUERY = """
            mutation LikeKolPost(${'$'}$PARAM_ID_POST: Int!, ${'$'}$PARAM_ACTION: Int!) {
                do_like_kol_post(idPost: ${'$'}$PARAM_ID_POST, action: ${'$'}$PARAM_ACTION) {
                    error
                    data {
                        success
                    }
                }
            }
        """

        fun createParam(contentId: String, action: String): Map<String, Any> {
            val contentIdInt = contentId.toIntOrNull()?:0
            val actionInt = action.toIntOrNull() ?: 0
            return mapOf(
                PARAM_ID_POST to contentIdInt,
                PARAM_ACTION to actionInt,
            )
        }
    }
}