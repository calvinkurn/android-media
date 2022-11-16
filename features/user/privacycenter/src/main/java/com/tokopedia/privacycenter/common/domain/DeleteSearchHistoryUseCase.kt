package com.tokopedia.privacycenter.common.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.privacycenter.common.data.DeleteSearchHistoryParam
import com.tokopedia.privacycenter.common.data.DeleteSearchHistoryResponse
import com.tokopedia.privacycenter.common.utils.generateUrlParamString
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class DeleteSearchHistoryUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<DeleteSearchHistoryParam, DeleteSearchHistoryResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            mutation universe_delete_recent_search(
                ${'$'}param: String!
            ){
              universe_delete_recent_search(param: ${'$'}param) {
                status
              }
            }
        """.trimIndent()

    override suspend fun execute(params: DeleteSearchHistoryParam): DeleteSearchHistoryResult {
        val registrationId = userSession.deviceId
        val userId = userSession.userId
        val uniqueId = getUniqueId(userId, registrationId)

        val variable = RequestParams.create()

        if (!params.clearAll) {
            variable.putAll(
                mapOf(
                    KEY_QUERY to params.query,
                    KEY_TYPE to params.type,
                    KEY_ID to params.id
                )
            )
        }

        variable.putAll(
            mapOf(
                KEY_CLEAR_ALL to params.clearAll.toString(),
                KEY_DEVICE to VALUE_DEVICE,
                KEY_SOURCE to VALUE_SOURCE,
                KEY_USER_ID to userId,
                KEY_UNIQUE_ID to uniqueId,
                KEY_DEVICE_ID to registrationId
            )
        )

        val parameter = mapOf(PARAM to generateUrlParamString(variable.parameters))

        val response: DeleteSearchHistoryResponse = repository.request(graphqlQuery(), parameter)

        val status = response.universeDeleteRecentSearch.status

        return if (status == KEY_DELETE_SUCCESS) {
            DeleteSearchHistoryResult.Success(params.position, params.clearAll)
        } else {
            DeleteSearchHistoryResult.Failed(params.position, params.clearAll)
        }
    }

    private fun getUniqueId(userId: String, registrationId: String) =
        if (userId.isNotEmpty()) AuthHelper.getMD5Hash(userId)
        else AuthHelper.getMD5Hash(registrationId)

    companion object {
        private const val PARAM = "param"
        private const val KEY_QUERY = "q"
        private const val KEY_TYPE = "type"
        private const val KEY_ID = "id"
        private const val KEY_CLEAR_ALL = "clear_all"
        private const val KEY_DEVICE = "device"
        private const val VALUE_DEVICE = "android"
        private const val KEY_SOURCE = "navsource"
        private const val VALUE_SOURCE = "privacy_center"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_DELETE_SUCCESS = "success"
    }
}

sealed class DeleteSearchHistoryResult(
    val position: Int = -1, val isClearAll: Boolean = false, val throwable: Throwable? = null
) {
    class Success(position: Int, isClearAll: Boolean) : DeleteSearchHistoryResult(
        position = position, isClearAll = isClearAll
    )
    class Failed(position: Int, isClearAll: Boolean, throwable: Throwable? = null) : DeleteSearchHistoryResult(
        position = position, isClearAll = isClearAll, throwable = throwable
    )
}
