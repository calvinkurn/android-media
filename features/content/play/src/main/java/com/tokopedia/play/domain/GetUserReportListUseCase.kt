package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.UserReportOptions
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/12/21
 */
@GqlQuery(GetUserReportListUseCase.QUERY_NAME, GetUserReportListUseCase.QUERY)
class GetUserReportListUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<UserReportOptions.Response>(graphqlRepository) {

    init {
        setRequestParams(createParam())
        setGraphqlQuery(GetUserReportListUseCaseQuery.GQL_QUERY)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UserReportOptions.Response::class.java)
    }

    private fun createParam(languageId: String = LANGUAGE_PARAM_ID): HashMap<String, Any> = hashMapOf(
        LANGUAGE_PARAM to languageId
    )

    companion object {
        private const val LANGUAGE_PARAM = "lang"
        private const val LANGUAGE_PARAM_ID = "id"
        const val QUERY_NAME = "GetUserReportListUseCaseQuery"
        const val QUERY = """
            query getUserReportReason(${'$'}$LANGUAGE_PARAM: String) {
              visionGetReportVideoReason($LANGUAGE_PARAM:${'$'}$LANGUAGE_PARAM) {
                category_id
                detail
                value
                additional_fields {
                  key
                  max
                  min
                  type
                  label
                }
                child {
                  category_id
                  detail
                  value
                  additional_fields {
                    key
                    max
                    min
                    type
                    label
                  }
                }
              }
            }
        """
    }
}