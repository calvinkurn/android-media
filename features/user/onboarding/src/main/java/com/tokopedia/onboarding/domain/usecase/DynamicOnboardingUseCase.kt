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
) : CoroutineUseCase<Unit, ConfigDataModel>(dispatcher) {

//    fun getDynamicOnboardingData(onSuccess: (ConfigDataModel) -> Unit, onError: (Throwable) -> Unit) {
//        rawQueries[OnboardingConstant.Query.QUERY_DYNAMIC_ONBAORDING]?.let { query ->
//            graphqlUseCase.apply {
//                setTypeClass(DynamicOnboardingResponseDataModel::class.java)
//                setGraphqlQuery(query)
//                execute({ data ->
//                    if (data.dyanmicOnboarding.isEnable && data.dyanmicOnboarding.config.isNotEmpty()) {
//                        val config = Gson().fromJson(data.dyanmicOnboarding.config, ConfigDataModel::class.java)
//                        onSuccess(config)
//                    } else {
//                        onError(Throwable(data.dyanmicOnboarding.message))
//                    }
//                }, { throwable ->
//                    onError(throwable)
//                })
//            }
//        }
//    }

    override fun graphqlQuery(): String {
        return "query {\n" +
                "    GetDynamicOnboarding {\n" +
                "        enable\n" +
                "        config\n" +
                "        message\n" +
                "        error_message\n" +
                "    }\n" +
                " }"
    }

    override suspend fun execute(params: Unit): ConfigDataModel {
        return repository.request(graphqlQuery(), params)
    }
}