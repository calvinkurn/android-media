package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.markasseen.MarkAsSeenResponse
import javax.inject.Inject
import javax.inject.Named

class NotificationMarkAsSeenUseCase @Inject constructor(
        @Named(MUTATION_MARK_AS_SEEN)
        private val query: String,
        private val gqlUseCase: GraphqlUseCase<MarkAsSeenResponse>
) {

    fun markAsSeen(
            @RoleType role: Int,
            notifIds: List<String>
    ) {
        val param = generateParam(role, notifIds)
        gqlUseCase.apply {
            setTypeClass(MarkAsSeenResponse::class.java)
            setRequestParams(param)
            setGraphqlQuery(query)
        }.execute({}, {})
    }

    private fun generateParam(role: Int, notifIds: List<String>): Map<String, Any?> {
        return mapOf(
                PARAM_TYPE_ID to if (role == RoleType.BUYER) 1 else 2,
                PARAM_NOTIF_ID to notifIds
        )
    }

    companion object {
        const val MUTATION_MARK_AS_SEEN = "mutation_mark_as_seen"
        const val PARAM_TYPE_ID = "type_id"
        const val PARAM_NOTIF_ID = "notif_id"
        val query = R.raw.query_notification_mark_as_seen
    }
}