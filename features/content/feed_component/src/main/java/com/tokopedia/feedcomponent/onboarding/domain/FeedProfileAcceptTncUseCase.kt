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
        private const val KEY_USERNAME = "username"
        private const val KEY_BIOGRAPHY = "biography"
        private const val KEY_IS_UPDATE_USERNAME = "isUpdateUsername"
        private const val KEY_IS_UPDATE_BIOGRAPHY = "isUpdateBiography"

        const val QUERY_NAME = "FeedProfileAcceptTncUseCaseQuery"
        const val QUERY = """
            mutation FeedXProfileSubmit(
                ${"$$KEY_USERNAME"}: String!,
                ${"$$KEY_BIOGRAPHY"}: String!,
                ${"$$KEY_IS_UPDATE_USERNAME"}: Boolean!,
                ${"$$KEY_IS_UPDATE_BIOGRAPHY"}: Boolean!,
            ){
              feedXProfileSubmit(req:{
                $KEY_USERNAME: ${"$$KEY_USERNAME"},
                $KEY_BIOGRAPHY: ${"$$KEY_BIOGRAPHY"},
                $KEY_IS_UPDATE_USERNAME: ${"$$KEY_IS_UPDATE_USERNAME"},
                $KEY_IS_UPDATE_BIOGRAPHY: ${"$$KEY_IS_UPDATE_BIOGRAPHY"}
              }) {
                status
              }
            }
        """

        fun createInsertNewUsernameParam(username: String) = mapOf<String, Any>(
            KEY_USERNAME to username,
            KEY_BIOGRAPHY to "",
            KEY_IS_UPDATE_USERNAME to false,
            KEY_IS_UPDATE_BIOGRAPHY to false,
        )
    }
}