package com.tokopedia.people.domains

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.people.model.SetProfileSettingsRequest
import com.tokopedia.people.model.SetProfileSettingsResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 10, 2023
 */
@GqlQuery(SetProfileSettingsUseCase.QUERY_NAME, SetProfileSettingsUseCase.QUERY)
class SetProfileSettingsUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<SetProfileSettingsRequest, SetProfileSettingsResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = SetProfileSettingsUseCaseQuery().getQuery()

    override suspend fun execute(params: SetProfileSettingsRequest): SetProfileSettingsResponse {
        val param = mapOf(
            PARAM_REQ to mapOf(
                PARAM_AUTHOR_ID to params.authorID,
                PARAM_AUTHOR_TYPE to params.authorType,
                PARAM_DATA to params.data
            )
        )

        return repository.request(graphqlQuery(), param)
    }

    companion object {
        private const val PARAM_REQ = "req"
        private const val PARAM_AUTHOR_ID = "authorID"
        private const val PARAM_AUTHOR_TYPE = "authorType"
        private const val PARAM_DATA = "data"

        const val QUERY_NAME = "SetProfileSettingsUseCaseQuery"
        const val QUERY = """
            mutation FeedXProfileSetProfileSettings(
                ${"$${PARAM_REQ}"}: feedXProfileSetProfileSettingsRequest!
            ) {
                feedXProfileSetProfileSettings(
                    ${PARAM_REQ}: ${"$${PARAM_REQ}"}
                ) {
                  success
                }
            }
        """
    }
}
