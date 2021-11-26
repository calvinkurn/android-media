package com.tokopedia.notifications.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.data.model.AmplificationNotifier
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AmplificationUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<AmplificationNotifier>,
        private val rawQuery: String
) {

    fun execute(onSuccess: (response: AmplificationNotifier) -> Unit,
                onError : (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(AmplificationNotifier::class.java)
            setRequestParams(RequestParams.EMPTY.parameters)
            setGraphqlQuery(rawQuery)
            execute(onSuccess, onError)
        }
    }

}