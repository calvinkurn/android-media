package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.SafeModeParam
import com.tokopedia.home_account.data.model.SetUserProfileSettingResponse
import javax.inject.Inject

class UpdateSafeModeUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SafeModeParam, SetUserProfileSettingResponse>(dispatcher.io) {

    override suspend fun execute(params: SafeModeParam): SetUserProfileSettingResponse {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation userProfileSettingUpdate(${'$'}safeMode: Boolean!) {
          userProfileSettingUpdate(safeMode: ${'$'}safeMode) {
            isSuccess
            error
          }
        }""".trimIndent()
}
