package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.domain.pojo.NotificationUpdateFilter
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

class NotificationFilterUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.FILTER_NOTIFICATION)
        private val query: String,
        private val useCase: GraphqlUseCase<NotificationUpdateFilter>) {

    fun get(requestParams: Map<String, Any>,
            onSuccess: (NotificationUpdateFilter) -> Unit,
            onError: (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(NotificationUpdateFilter::class.java)
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
        private const val PARAM_NOTIF_TYPE  = "typeOfNotif"
        private const val TYPE_NOTIF_UPDATE = 2

        fun params(): HashMap<String, Any> {
            val variables = HashMap<String, Any>()
            variables[PARAM_NOTIF_TYPE] = TYPE_NOTIF_UPDATE
            return variables
        }
    }

}