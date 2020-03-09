package com.tokopedia.onboarding.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.onboarding.data.OnbaordingConstant
import com.tokopedia.onboarding.domain.model.*
import javax.inject.Inject

class DynamicOnbaordingUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        private val graphqlUseCase: GraphqlUseCase<DynamicOnboardingDataModel>
) {

    fun getData(onSuccess: (DynamicOnboardingDataModel) -> Unit, onError: (Throwable) -> Unit) {
        rawQueries[OnbaordingConstant.Query.QUERY_DYNAMIC_ONBAORDING]?.let { query ->
            graphqlUseCase.apply {
                setTypeClass(DynamicOnboardingDataModel::class.java)
                setGraphqlQuery(query)
                execute({ data ->
                    onSuccess(data)
                }, { throwable ->
                    onError(throwable)
                })
            }
        }
    }
}