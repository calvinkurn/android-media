package com.tokopedia.loginregister.login.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginregister.login.di.LoginQueryConstant
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-02-05.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class StatusFingerprintUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<StatusFingerprintpojo>(graphqlRepository) {

    fun executeCoroutines(onSuccess: (StatusFingerprint) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit, signature: SignatureData){
        rawQueries[LoginQueryConstant.QUERY_VERIFY_FINGERPRINT]?.let { query ->
            setRequestParams(mapOf(
                    LoginQueryConstant.PARAM_SIGNATURE to signature.signature,
                    LoginQueryConstant.PARAM_DATETIME to signature.datetime
            ))
            setTypeClass(StatusFingerprintpojo::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it.data)
            }, onError)
        }
    }
}