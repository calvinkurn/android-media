package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.RemoveFingerprintPojo
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 07/10/21.
 */

class RemoveFingerprintUsecase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val fingerprintPreference: FingerprintPreference
) : CoroutineUseCase<Unit, RemoveFingerprintPojo>(Dispatchers.IO){

    override suspend fun execute(params: Unit): RemoveFingerprintPojo {
        val mappedParams = mapOf(BiometricConstant.PARAM_BIOMETRIC_ID to fingerprintPreference.getUniqueId())
        return repository.request(graphqlQuery(), mappedParams)
    }

    override fun graphqlQuery(): String = """
            mutation remove_fingerprint {
                flushFingerprintByUniqueID {
                        is_success
                        error
                        message                   
                }
            }""".trimIndent()
}