package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.data.model.RegisterCheckFingerprint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RegisterCheckFingerprintUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<RegisterCheckFingerprint>,
    private var dispatchers: CoroutineDispatchers
): CoroutineScope {
    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun checkRegisteredFingerprint(onSuccess: (RegisterCheckFingerprint) -> Unit,
                                   onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.io, {
            val data =
                graphqlUseCase.apply {
                    setTypeClass(RegisterCheckFingerprint::class.java)
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
            query register_check_fp {
                OTPBiometricCheckRegistered {
                        errorMessage
                        Registered
                }
            }""".trimIndent()
    }

}