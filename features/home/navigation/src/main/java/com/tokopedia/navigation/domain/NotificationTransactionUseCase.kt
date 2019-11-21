package com.tokopedia.navigation.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.navigation.data.consts.NotificationQueriesConstant
import com.tokopedia.navigation.domain.pojo.NotificationCenterDetail
import javax.inject.Inject
import javax.inject.Named

class NotificationTransactionUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.TRANSACTION_NOTIFICATION)
        private val query: String,
        private val useCase: GraphqlUseCase<NotificationCenterDetail>) {

    fun get(requestParams: Map<String, Any>,
            onSuccess: (NotificationCenterDetail) -> Unit,
            onError: (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(NotificationCenterDetail::class.java)
            setRequestParams(requestParams)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    companion object {
        private const val PARAM_PAGE = "page"
        private const val PARAM_LAST_ID = "lastNotifId"

        fun params(
                page: Int,
                variables: HashMap<String, Any>,
                lastNotifId: String
        ): HashMap<String, Any> {
            variables[PARAM_PAGE] = page
            variables[PARAM_LAST_ID] = lastNotifId
            return variables
        }
    }

}