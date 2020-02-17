package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.FingerprintSignature
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

    fun executeUseCase(onSuccess: (ValidateFingerprintResult) -> Unit, onError: (Throwable) -> Unit){
        rawQueries[LoginFingerprintQueryConstant.QUERY_VALIDATE_FINGERPRINT]?.let { query ->
            setTypeClass(ValidateFingerprintPojo::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it.data)
            }, onError)
        }
    }

    fun createRequestParams(userId: String, otpType: String, signature: FingerprintSignature): Map<String, String>{
        return mapOf(
                LoginFingerprintQueryConstant.PARAM_USER_ID to userId,
                LoginFingerprintQueryConstant.PARAM_OTP_TYPE to otpType,
                LoginFingerprintQueryConstant.PARAM_SIGNATURE to signature.signature,
                LoginFingerprintQueryConstant.PARAM_TIME_UNIX to signature.datetime
        )
    }
}