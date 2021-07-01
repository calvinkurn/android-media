package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.RemoveFingerprintPojo
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RemoveFingerprintUsecase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<RemoveFingerprintPojo>,
    private var dispatchers: CoroutineDispatchers,
    private val fingerprintPreferenceManager: FingerprintPreference
): CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun removeFingerprint(
        onSuccess: (RemoveFingerprintPojo) -> Unit,
        onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.io, {
            val data =
                graphqlUseCase.apply {
                    setTypeClass(RemoveFingerprintPojo::class.java)
                    setRequestParams(mapOf(
                        BiometricConstant.PARAM_BIOMETRIC_ID to fingerprintPreferenceManager.getUniqueId()
                    ))
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