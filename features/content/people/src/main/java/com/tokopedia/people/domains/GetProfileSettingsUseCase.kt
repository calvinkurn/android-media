package com.tokopedia.people.domains

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.people.model.GetProfileSettingsRequest
import com.tokopedia.people.model.GetProfileSettingsResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
@GqlQuery(GetProfileSettingsUseCase.QUERY_NAME, GetProfileSettingsUseCase.QUERY)
class GetProfileSettingsUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<GetProfileSettingsRequest, GetProfileSettingsResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = GetProfileSettingsUseCaseQuery().getQuery()

    override suspend fun execute(params: GetProfileSettingsRequest): GetProfileSettingsResponse {
        val param = mapOf(
            PARAM_REQ to mapOf(
                PARAM_AUTHOR_ID to params.authorID,
                PARAM_AUTHOR_TYPE to params.authorType,
            )
        )

        return repository.request(graphqlQuery(), param)
    }

    companion object {
        private const val PARAM_REQ = "req"
        private const val PARAM_AUTHOR_ID = "authorID"
        private const val PARAM_AUTHOR_TYPE = "authorType"

        const val QUERY_NAME = "GetProfileSettingsUseCaseQuery"
        const val QUERY = """
            query FeedXProfileGetProfileSettings(
                ${"$${PARAM_REQ}"}: feedXProfileGetProfileSettingsRequest!
            ) {
                feedXProfileGetProfileSettings(
                    ${PARAM_REQ}: ${"$${PARAM_REQ}"}
                ) {
                    settingsProfile {
                        settingID
                        title
                        enabled
                    }
                }
            }
        """
    }
}
