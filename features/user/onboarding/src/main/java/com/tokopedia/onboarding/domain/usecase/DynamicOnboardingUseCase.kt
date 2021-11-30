package com.tokopedia.onboarding.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.data.OnboardingConstant
import com.tokopedia.onboarding.domain.model.*
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DynamicOnboardingUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, DynamicOnboardingResponseDataModel>(dispatcher) {

    override fun graphqlQuery(): String {
        return """query { 
                    GetDynamicOnboarding
                        enable
                        config
                        message
                        error_message
                    } 
                 }"""
    }

    override suspend fun execute(params: Unit): DynamicOnboardingResponseDataModel {
        return repository.request(graphqlQuery(), params)
    }
}