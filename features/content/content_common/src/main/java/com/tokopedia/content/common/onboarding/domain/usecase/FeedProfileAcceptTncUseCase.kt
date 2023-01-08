package com.tokopedia.content.common.onboarding.domain.usecase

import com.tokopedia.content.common.onboarding.model.FeedProfileAcceptTncResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
@GqlQuery(FeedProfileAcceptTncUseCase.QUERY_NAME, FeedProfileAcceptTncUseCase.QUERY)
class FeedProfileAcceptTncUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<FeedProfileAcceptTncResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(FeedProfileAcceptTncUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedProfileAcceptTncResponse::class.java)
    }

    companion object {
        const val QUERY_NAME = "FeedProfileAcceptTncUseCaseQuery"
        const val QUERY = """
            mutation FeedXProfileAcceptTnC {
              feedXProfileAcceptTnC {
                    hasAcceptTnC
              }
            }
        """
    }
}