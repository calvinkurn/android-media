package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintResult
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryConstant
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-02-11.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ValidateFingerprintUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ValidateFingerprintPojo>(graphqlRepository) {

    fun executeUseCase(param: Map<String, Any>, onSuccess: (ValidateFingerprintResult) -> Unit, onError: (Throwable) -> Unit){
        rawQueries[LoginFingerprintQueryConstant.QUERY_VALIDATE_FINGERPRINT]?.let { query ->
            setRequestParams(param)
            setTypeClass(ValidateFingerprintPojo::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it.data)
            }, onError)
        }
    }

    fun createRequestParams(userId: String, signature: SignatureData): Map<String, Any>{
        return mapOf(
                LoginFingerprintQueryConstant.PARAM_OTP_TYPE to LoginFingerprintQueryConstant.VALIDATE_OTP_TYPE,
                LoginFingerprintQueryConstant.PARAM_SIGNATURE to signature.signature,
                LoginFingerprintQueryConstant.PARAM_TIME_UNIX to signature.datetime,
                LoginFingerprintQueryConstant.PARAM_USER_ID to userId.toInt(),
                LoginFingerprintQueryConstant.PARAM_MODE to LoginFingerprintQueryConstant.VALIDATE_MODE
        )
    }
}