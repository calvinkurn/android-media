package com.tokopedia.discovery2.repository.pushstatus.pushstatus

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.data.push.PushUnSubscriptionResponse
import javax.inject.Inject


class PushStatusGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), PushStatusRepository {

    override suspend fun checkPushStatus(compaignId: Long): PushStatusResponse {
        return getGQLData(queryCheckPushReminderGql,
            PushStatusResponse::class.java, mapOf("campaignID" to compaignId))
    }

    override suspend fun subscribeToPush(compaignId: Long): PushSubscriptionResponse {
        return getGQLData(querySetPushReminderGql,
            PushSubscriptionResponse::class.java, mapOf("campaignID" to compaignId))
    }

    override suspend fun unsSubscribeToPush(compaignId: Long): PushUnSubscriptionResponse {
        return getGQLData(queryUnsetPushReminderGql,
            PushUnSubscriptionResponse::class.java, mapOf("campaignID" to compaignId))
    }

    private val queryCheckPushReminderGql: String = """query reminderCheck(${'$'}campaignID: Int!) {
  notifier_checkReminder(campaign: ${'$'}campaignID) {
    is_success
    status
    error_message
  }
}""".trimIndent()

    private val querySetPushReminderGql: String = """query setReminder(${'$'}campaignID: Int!) {
  notifier_setReminder(campaign: ${'$'}campaignID) {
    is_success
    error_message
  }
}
""".trimIndent()

    private val queryUnsetPushReminderGql: String = """query unsetReminder(${'$'}campaignID: Int!) {
  notifier_unsetReminder(campaign: ${'$'}campaignID) {
    is_success
    error_message
  }
}
""".trimIndent()
}
