package com.tokopedia.feedcomponent.onboarding.domain

import com.tokopedia.feedcomponent.onboarding.model.FeedProfileSubmitResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
@GqlQuery(FeedProfileSubmitUseCase.QUERY_NAME, FeedProfileSubmitUseCase.QUERY)
class FeedProfileSubmitUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<FeedProfileSubmitResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(FeedProfileSubmitUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedProfileSubmitResponse::class.java)
    }

    companion object {
        const val QUERY_NAME = "FeedProfileSubmitUseCaseQuery"
        const val QUERY = """
            mutation FeedXProfileAcceptTnC{
              feedXProfileAcceptTnC(){
                    hasAcceptTnC
                }
            }
        """
    }
}