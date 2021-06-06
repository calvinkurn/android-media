package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CheckFingerprintToggleStatusUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<CheckFingerprintPojo>,
    private var dispatchers: CoroutineDispatchers
): CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun checkFingerprint(userId: String, onSuccess: (CheckFingerprintPojo) -> Unit,
                         onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.io, {
            val data =
                graphqlUseCase.apply {
                    setTypeClass(CheckFingerprintPojo::class.java)
                    setRequestParams(mapOf(
                        PARAM_USER_ID to userId
                    ))
                    setGraphqlQuery(RegisterFingerprintUseCase.query)
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
        const val PARAM_USER_ID = "userID"

        val query: String = """
            query check_otp_toggle(${'$'}userID: String!) {
                OTPBiometricCheckToggleStatus(userID: ${'$'}userID) {
                    is_active
                    is_success
                    errorMessage
               }
            }""".trimIndent()
    }
}