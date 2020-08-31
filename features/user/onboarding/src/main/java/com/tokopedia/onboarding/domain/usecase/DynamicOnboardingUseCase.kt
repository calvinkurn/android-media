package com.tokopedia.onboarding.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.onboarding.data.OnboardingConstant
import com.tokopedia.onboarding.domain.model.*
import javax.inject.Inject

class DynamicOnboardingUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        private val graphqlUseCase: GraphqlUseCase<DynamicOnboardingResponseDataModel>
) {

    fun getDynamicOnboardingData(onSuccess: (ConfigDataModel) -> Unit, onError: (Throwable) -> Unit) {
        rawQueries[OnboardingConstant.Query.QUERY_DYNAMIC_ONBAORDING]?.let { query ->
            graphqlUseCase.apply {
                setTypeClass(DynamicOnboardingResponseDataModel::class.java)
                setGraphqlQuery(query)
                execute({ data ->
                    if (data.dyanmicOnboarding.isEnable && data.dyanmicOnboarding.config.isNotEmpty()) {
                        val config = Gson().fromJson(data.dyanmicOnboarding.config, ConfigDataModel::class.java)
                        onSuccess(config)
                    } else {
                        onError(Throwable(data.dyanmicOnboarding.message))
                    }
                }, { throwable ->
                    onError(throwable)
                })
            }
        }
    }

    fun cancelJobs() {
        graphqlUseCase.cancelJobs()
    }
}