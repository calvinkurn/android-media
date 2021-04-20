package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryConstant
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-02-06.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RegisterFingerprintUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<RegisterFingerprintPojo>(graphqlRepository) {

    fun executeUseCase(onSuccess: (RegisterFingerprintPojo) -> Unit, onError: (Throwable) -> Unit){
        rawQueries[LoginFingerprintQueryConstant.QUERY_REGISTER_FINGERPRINT]?.let { query ->
            setTypeClass(RegisterFingerprintPojo::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it)
            }, onError)
        }
    }

    fun createRequestParam(signature: SignatureData, publicKey: String): Map<String, String>{
        return mapOf(
                LoginFingerprintQueryConstant.PARAM_PUBLIC_KEY to publicKey,
                LoginFingerprintQueryConstant.PARAM_SIGNATURE to signature.signature,
                LoginFingerprintQueryConstant.PARAM_DATETIME to signature.datetime
        )
    }
}