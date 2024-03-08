package com.tokopedia.onboarding.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.onboarding.domain.model.*
import javax.inject.Inject

class DynamicOnboardingUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, DynamicOnboardingResponseDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return """query { 
                    GetDynamicOnboarding {
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
