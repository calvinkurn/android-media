package com.tokopedia.loginregister.login.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckFingerprint
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RegisterCheckFingerprintUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<RegisterCheckFingerprint>,
    private var dispatchers: CoroutineDispatchers,
    private val fingerprintPreferenceManager: FingerprintPreference
): CoroutineScope {
    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun checkRegisteredFingerprint(onSuccess: (RegisterCheckFingerprint) -> Unit,
                                   onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.io, {
            val data =
                graphqlUseCase.apply {
                    setTypeClass(RegisterCheckFingerprint::class.java)
                    setRequestParams(mapOf(
                        PARAM_BIOMETRIC_ID to fingerprintPreferenceManager.getUniqueId()
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
        const val PARAM_BIOMETRIC_ID = "device_biometrics"

        val query: String = """
            query register_check_fp {
                OTPBiometricCheckRegistered {
                        registered
                        errorMessage
                }
            }""".trimIndent()
    }

}