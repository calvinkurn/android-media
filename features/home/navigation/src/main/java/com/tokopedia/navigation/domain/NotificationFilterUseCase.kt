package com.tokopedia.navigation.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.navigation.data.consts.NotificationQueriesConstant
import com.tokopedia.navigation.domain.pojo.NotificationUpdateFilter
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

class NotificationFilterUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.FILTER_NOTIFICATION)
        private val query: String,
        private val useCase: GraphqlUseCase<NotificationUpdateFilter>) {

    private val params = RequestParams.EMPTY

    fun get(onSuccess: (NotificationUpdateFilter) -> Unit, onError: (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(NotificationUpdateFilter::class.java)
            setRequestParams(params.parameters)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

}