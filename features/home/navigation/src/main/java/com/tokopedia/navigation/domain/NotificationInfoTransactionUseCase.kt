package com.tokopedia.navigation.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.navigation.data.consts.NotificationQueriesConstant
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

class NotificationInfoTransactionUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.DRAWER_PUSH_NOTIFICATION)
        private val query: String,
        private val useCase: GraphqlUseCase<NotificationEntity>) {

    private val params = RequestParams.EMPTY

    fun get(onSuccess: (NotificationEntity) -> Unit, onError: (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(NotificationEntity::class.java)
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