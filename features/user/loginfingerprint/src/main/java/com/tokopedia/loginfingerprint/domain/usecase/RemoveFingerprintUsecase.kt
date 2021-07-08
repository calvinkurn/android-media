package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.data.model.RemoveFingerprintPojo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RemoveFingerprintUsecase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<RemoveFingerprintPojo>,
    private var dispatchers: CoroutineDispatchers
): CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun removeFingerprint(
        onSuccess: (RemoveFingerprintPojo) -> Unit,
        onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.io, {
            val data =
                graphqlUseCase.apply {
                    setTypeClass(RemoveFingerprintPojo::class.java)
                    setGraphqlQuery(query)
                }.executeOnBackground()
            withContext(dispatchers.main) {
                onSuccess(data)
            }
        }, {
            withContext(dispatchers.main) {
                onError(it)
            }
        })
    }

    companion object {
        val query: String = """
            mutation remove_fingerprint {
                flushFingerprintByUniqueID {
                        is_success
                        error
                        message                   
                }
            }""".trimIndent()
    }
}