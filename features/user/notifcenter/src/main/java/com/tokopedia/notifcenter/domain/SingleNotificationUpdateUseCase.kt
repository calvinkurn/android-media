package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.NotificationCenterDetail
import com.tokopedia.notifcenter.data.entity.NotificationCenterSingleDetail
import javax.inject.Inject
import javax.inject.Named

class SingleNotificationUpdateUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.SINGLE_NOTIFICATION_UPDATE)
        private val query: String,
        private val useCase: GraphqlUseCase<NotificationCenterSingleDetail>
){

    fun get(
            params: Map<String, Any>,
            onSuccess: (NotificationCenterSingleDetail) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        useCase.apply {
            setTypeClass(NotificationCenterSingleDetail::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    companion object {
        private const val PARAM_NOTIF_ID = "notifId"

        fun params(notificationId: String): Map<String, Any> {
            val variables = hashMapOf<String, Any>()
            variables[PARAM_NOTIF_ID] = notificationId
            return variables
        }
    }

}