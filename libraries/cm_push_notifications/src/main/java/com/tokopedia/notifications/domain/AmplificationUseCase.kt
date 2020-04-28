package com.tokopedia.notifications.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.data.model.AmplificationNotifier
import javax.inject.Inject

class AmplificationUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<AmplificationNotifier>,
        private val rawQuery: String
) {

    fun execute(
            params: Map<String, Any>,
            onSuccess: (response: AmplificationNotifier) -> Unit
    ) {
        useCase.apply {
            setTypeClass(AmplificationNotifier::class.java)
            setRequestParams(params)
            setGraphqlQuery(rawQuery)
            execute(onSuccess, {})
        }
    }

    fun params(deviceToken: String): Map<String, Any> {
        return hashMapOf<String, Any>().apply {
            put(DEVICE_TOKEN, deviceToken)
        }
    }

    companion object {
        private const val DEVICE_TOKEN = "device_token"
    }

}