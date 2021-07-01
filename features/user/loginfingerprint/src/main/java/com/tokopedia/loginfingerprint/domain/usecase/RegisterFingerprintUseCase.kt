package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryConstant
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Yoris Prayogo on 2020-02-06.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RegisterFingerprintUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<RegisterFingerprintPojo>,
        private var dispatchers: CoroutineDispatchers,
        private val fingerprintPreferenceManager: FingerprintPreference
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun registerFingerprint(
        uniqueId: String,
        signature: SignatureData,
        publicKey: String,
        onSuccess: (RegisterFingerprintPojo) -> Unit,
        onError: (Throwable) -> Unit) {
            launchCatchError(dispatchers.io, {
                val data =
                    graphqlUseCase.apply {
                            setTypeClass(RegisterFingerprintPojo::class.java)
                            setGraphqlQuery(query)
                            setRequestParams(createRequestParam(uniqueId, signature, publicKey))
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

    private fun createRequestParam(uniqueId: String, signature: SignatureData, publicKey: String): Map<String, String>{
        return mapOf(
                LoginFingerprintQueryConstant.PARAM_PUBLIC_KEY to publicKey,
                LoginFingerprintQueryConstant.PARAM_SIGNATURE to signature.signature,
                LoginFingerprintQueryConstant.PARAM_DATETIME to signature.datetime,
                BiometricConstant.PARAM_BIOMETRIC_ID to uniqueId
        )
    }

    companion object {
        val query: String = """
            query register_fingerprint (${'$'}publicKey: String!, ${'$'}signature: String!, ${'$'}datetime: String!){
                RegisterFingerprint(publicKey: ${'$'}publicKey, signature: ${'$'}signature, datetime: ${'$'}datetime) {
                    success
                    message
                    errorMessage
               }
            }""".trimIndent()
    }
}