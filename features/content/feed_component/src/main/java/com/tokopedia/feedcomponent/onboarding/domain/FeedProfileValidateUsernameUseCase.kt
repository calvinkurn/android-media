package com.tokopedia.feedcomponent.onboarding.domain

import com.tokopedia.feedcomponent.onboarding.model.FeedProfileAcceptTncResponse
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
) : GraphqlUseCase<FeedProfileValidateUsernameUseCase>(graphqlRepository) {

    init {
        setGraphqlQuery(FeedProfileValidateUsernameUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedProfileValidateUsernameUseCase::class.java)
    }

    companion object {
        const val QUERY_NAME = "FeedProfileValidateUsernameUseCaseQuery"
        const val QUERY = """
            mutation FeedXProfileAcceptTnC{
              feedXProfileAcceptTnC(){
                    hasAcceptTnC
                }
            }
        """
    }
}