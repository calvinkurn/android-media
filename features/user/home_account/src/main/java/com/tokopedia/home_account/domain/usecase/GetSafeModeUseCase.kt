package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.account_settings.data.model.UserProfileSettingResponse
import javax.inject.Inject

class GetSafeModeUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<Unit, UserProfileSettingResponse>(dispatcher.io) {

    override suspend fun execute(params: Unit): UserProfileSettingResponse {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query {
            userProfileSetting {
                safeMode
            }
        }""".trimIndent()
}
