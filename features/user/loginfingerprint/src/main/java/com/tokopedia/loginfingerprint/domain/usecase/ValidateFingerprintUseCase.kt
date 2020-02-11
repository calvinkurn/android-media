package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
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
}