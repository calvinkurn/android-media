package com.tokopedia.usercomponents.explicit.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.usercomponents.explicit.domain.model.UpdateStateDataModel
import com.tokopedia.usercomponents.explicit.domain.model.UpdateStateParam
import javax.inject.Inject

class UpdateStateUseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<UpdateStateParam, UpdateStateDataModel>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            mutation explicitprofileUpdateUserState(${'$'}input: UpdateUserStateRequest!) {
              explicitprofileUpdateUserState(input: ${'$'}input){
                message
                errorCode
                configActiveSuccess
                templateActiveSuccess
              }
            }
        """.trimIndent()

    override suspend fun execute(params: UpdateStateParam): UpdateStateDataModel {
        val parameter = mapOf(
            PARAM_INPUT to params
        )
        return repository.request(graphqlQuery(), parameter)
    }

    companion object {
        private const val PARAM_INPUT = "input"
    }
}