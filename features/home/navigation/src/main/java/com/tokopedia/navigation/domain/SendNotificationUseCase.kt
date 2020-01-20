package com.tokopedia.navigation.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.navigation.domain.pojo.NotifCenterSendNotifData
import com.tokopedia.navigation.domain.pojo.SendNotification
import com.tokopedia.navigation.data.consts.NotificationQueriesConstant
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-09-18.
 * ade.hadian@tokopedia.com
 */

class SendNotificationUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        private val userSession: UserSessionInterface,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<SendNotification>(graphqlRepository) {

    fun executeCoroutines(onSuccess: (NotifCenterSendNotifData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit){
        rawQueries[NotificationQueriesConstant.MUTATION_NOTIF_CENTER_PUSH_NOTIF]?.let { query ->
            setRequestParams(getRequestParams())
            setTypeClass(SendNotification::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it.data)
            }, onError)
        }
    }

    private fun getRequestParams(): Map<String, Any>{
        return mapOf(
                NotificationQueriesConstant.PARAM_SECTION_TYPE to SECTION_TYPE,
                NotificationQueriesConstant.PARAM_TEMPLATE_KEY to TEMPLATE_KEY,
                NotificationQueriesConstant.PARAM_EXPIRED_TIME to EXPIRED_TIME,
                NotificationQueriesConstant.PARAM_UNIQUE_ID to userSession.userId)
    }

    companion object {
        val SECTION_TYPE = "userapp_"
        val TEMPLATE_KEY = "first_access_notification"
        val EXPIRED_TIME = 0
    }

}