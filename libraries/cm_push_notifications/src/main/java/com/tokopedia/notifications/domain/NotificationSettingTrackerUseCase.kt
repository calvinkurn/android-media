package com.tokopedia.notifications.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.domain.data.NotificationSettingTrackerGqlResponse
import com.tokopedia.notifications.domain.data.SettingTrackerResponse
import com.tokopedia.notifications.domain.query.GQL_QUERY_SEND_NOTIF_SETTINGS_TRACKER_DATA
import com.tokopedia.notifications.domain.query.GetNotificationSettingTrackerGQLQuery
import javax.inject.Inject
import kotlin.collections.HashMap


@GqlQuery("GetCMHomeWidgetData", GQL_QUERY_SEND_NOTIF_SETTINGS_TRACKER_DATA)
class NotificationSettingTrackerUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<NotificationSettingTrackerGqlResponse>(graphqlRepository) {

    fun sendTrackerUserSettings(
        onSuccess: (SettingTrackerResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(NotificationSettingTrackerGqlResponse::class.java)
            this.setGraphqlQuery(GetNotificationSettingTrackerGQLQuery())
            this.setRequestParams(getRequestParams())
            this.execute(
                { result ->
                    if (result.settingTrackerResponse.isSuccess == 1) {
                        onSuccess(result.settingTrackerResponse)
                    } else {
                        onError(Throwable())
                    }
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(): HashMap<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[TYPE] = "pushnotif"
        requestParams[DATA] = getNotificationSettingsList()
        return requestParams
    }

    private fun getNotificationSettingsList(): ArrayList<HashMap<String, Any>> {
        val all = hashMapOf<String, Any>(NAME to "all", VALUE to true)
        return arrayListOf(all)
    }

    companion object {
        private const val NAME = "name"
        private const val VALUE = "value"
        private const val DATA = "data"
        private const val TYPE = "type"
    }
}
