package com.tokopedia.inbox.universalinbox.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxCounterWrapperResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.inbox.universalinbox.util.Result as Result

class UniversalInboxGetAllCounterUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers
) {

    private val counterFlow = MutableStateFlow<Result<UniversalInboxAllCounterResponse>>(
        Result.Loading
    )

    private fun graphqlQuery(): String = """
        query GetAllCounter($$PARAM_INPUT: NotificationRequest) {
          notifications(input: $$PARAM_INPUT) {
            inbox {
              talk
              ticket
              review
            }
            chat {
              unreadsSeller
              unreadsUser
            }
            notifcenter_unread {
              notif_unread
            }
          }
        }
    """.trimIndent()

    fun observe(): Flow<Result<UniversalInboxAllCounterResponse>> = counterFlow

    suspend fun refreshCounter(param: String) {
        withContext(dispatcher.io) {
            counterFlow.emit(Result.Loading) // Reset
            try {
                val response = repository.request<Map<String, Any>, UniversalInboxCounterWrapperResponse>(
                    query = graphqlQuery(),
                    params = mapOf(SHOP_ID to param)
                )
                counterFlow.emit(Result.Success(response.allCounter))
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                counterFlow.emit(Result.Error(throwable))
            }
        }
    }

    companion object {
        private const val PARAM_INPUT = "input"
        private const val SHOP_ID = "shopId"
    }
}
