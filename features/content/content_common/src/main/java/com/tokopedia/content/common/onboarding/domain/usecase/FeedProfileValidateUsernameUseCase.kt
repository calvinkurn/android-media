package com.tokopedia.content.common.onboarding.domain.usecase

import com.tokopedia.content.common.onboarding.model.FeedProfileValidateUsernameResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
@GqlQuery(FeedProfileValidateUsernameUseCase.QUERY_NAME, FeedProfileValidateUsernameUseCase.QUERY)
class FeedProfileValidateUsernameUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<FeedProfileValidateUsernameResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(FeedProfileValidateUsernameUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedProfileValidateUsernameResponse::class.java)
    }

    companion object {
        private const val KEY_USERNAME = "username"

        const val QUERY_NAME = "FeedProfileValidateUsernameUseCaseQuery"
        const val QUERY = """
            query FeedXProfileValidateUsername(${"$$KEY_USERNAME"}: String!){
              feedXProfileValidateUsername(
                $KEY_USERNAME: ${"$$KEY_USERNAME"}
              ){
                isValid
                notValidInformation
              }
            }
        """

        fun createParam(username: String) = mapOf<String, Any>(
            KEY_USERNAME to username
        )
    }
}