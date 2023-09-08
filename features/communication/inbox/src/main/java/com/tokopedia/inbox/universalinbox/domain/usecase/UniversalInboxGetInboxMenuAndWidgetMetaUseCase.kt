package com.tokopedia.inbox.universalinbox.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.universalinbox.data.UniversalInboxLocalRepository
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.inbox.universalinbox.util.Result as Result

class UniversalInboxGetInboxMenuAndWidgetMetaUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val localRepository: UniversalInboxLocalRepository,
    private val dispatcher: CoroutineDispatchers
) {

    private val menuFlow: MutableStateFlow<Result<UniversalInboxWrapperResponse?>> =
        MutableStateFlow(Result.Loading)

    private val gson: Gson by lazy {
        Gson()
    }
    private fun graphqlQuery(): String = """
        query GetChatInboxWidgetMeta {
          chatInboxWidgetMeta {
            metadata {
              icon
              title
              subtext
              androidApplink
              type
              isDynamic
            }
            inboxMenu{
              icon
              title
              appLink
              label{
                color
                text
              }
              type
            }
          }
        }
    """.trimIndent()

    fun observe(): Flow<Result<UniversalInboxWrapperResponse?>> = menuFlow

    suspend fun fetchInboxMenuAndWidgetMeta(params: Unit) {
        try {
            withContext(dispatcher.io) {
                val result = repository.request<Unit, UniversalInboxWrapperResponse>(
                    graphqlQuery(),
                    params
                )
                updateCache(result)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            menuFlow.emit(Result.Error(throwable))
        }
    }

    suspend fun observeInboxMenuLocalSource() {
        localRepository.getInboxMenuCacheObserver()
            .map {
                return@map try {
                    val resultObj = gson.fromJson(it, UniversalInboxWrapperResponse::class.java)
                    resultObj
                } catch (throwable: Throwable) {
                    Timber.d(throwable)
                    null
                }
            }
            .catch {
                Timber.d(it)
                emit(null)
            }
            .flowOn(dispatcher.io)
            .collectLatest {
                menuFlow.emit(Result.Success(it))
            }
    }

    suspend fun updateCache(
        value: UniversalInboxWrapperResponse
    ) {
        // try-catch block is used to prevent obstructing the flow
        // in case of a failure to save to the cache
        try {
            localRepository.setInboxMenuCache(value)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }
}
