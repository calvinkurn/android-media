package com.tokopedia.watch.notification.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.watch.notification.model.NotificationCenterDetailResponse
import com.tokopedia.watch.notification.model.NotificationListModel
import rx.Observable
import rx.functions.Func1
import java.util.*

class GetNotificationListUseCase(
    private val graphqlUseCase: GraphqlUseCase,
    private val notificationListModelMapper: Func1<GraphqlResponse?, NotificationListModel>
) : UseCase<NotificationListModel>() {
    override fun createObservable(p0: RequestParams?): Observable<NotificationListModel> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(
            mutableListOf(
                createNotificationListGqlRequest()
            )
        )
        return graphqlUseCase
            .createObservable(RequestParams.EMPTY)
            .map(notificationListModelMapper)
    }

    @GqlQuery("NotificationListQuery", NOTIFICATION_LIST_QUERY)
    private fun createNotificationListGqlRequest(): GraphqlRequest {
        return GraphqlRequest(
            NotificationListQuery.GQL_QUERY,
            NotificationCenterDetailResponse::class.java,
            createParams(
                filter = FILTER_NONE,
                role = SELLER,
                lastNotificationId = String.EMPTY,
                fields = arrayOf(NEW)

            ).parameters
        )
    }

    companion object {

        const val SELLER: Int = 2
        const val FILTER_NONE: Long = 0
        const val NEW = "new"

        private const val PARAM_TYPE_ID = "type_id"
        private const val PARAM_TAG_ID = "tag_id"
        private const val PARAM_TIMEZONE = "timezone"
        private const val PARAM_LAST_NOTIF_ID = "last_notif_id"
        private const val PARAM_FIELDS = "fields"

        private const val NOTIFICATION_LIST_QUERY = """
            query notifcenter_detail_v3(
                $$PARAM_TYPE_ID: Int
            	$$PARAM_TAG_ID: Int
            	$$PARAM_TIMEZONE: String
            	$$PARAM_LAST_NOTIF_ID: String
            	$$PARAM_FIELDS: [String]
            ) {
            	notifcenter_detail_v3(
            		type_id: $$PARAM_TYPE_ID
            		tag_id: $$PARAM_TAG_ID
            		timezone: $$PARAM_TIMEZONE
            		last_notif_id: $$PARAM_LAST_NOTIF_ID
            		fields: $$PARAM_FIELDS
            	) {
            		new_list {
            			notif_id
            			title
            			status
                        create_time_unix
            			read_status
                        data_notification {
                            info_thumbnail_url
                        }
            		}
            	}
            }
        """

        private fun createParams(
            filter: Long,
            role: Int,
            lastNotificationId: String,
            fields: Array<String>
        ): RequestParams {
            return RequestParams.create().apply {
                putObject(PARAM_TYPE_ID, role)
                putObject(PARAM_TAG_ID, filter)
                putObject(PARAM_TIMEZONE, TimeZone.getDefault().id)
                putObject(PARAM_LAST_NOTIF_ID, lastNotificationId)
                putObject(PARAM_FIELDS, fields)
            }
        }
    }
}
