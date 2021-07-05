package com.tokopedia.promotionstarget.domain.usecase

import com.tokopedia.promotionstarget.data.UpdataGratifNotificationParams
import com.tokopedia.promotionstarget.data.autoApply.UpdateGratificationNotificationResponse
import com.tokopedia.promotionstarget.data.di.UPDATE_GRATIF
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import javax.inject.Inject
import javax.inject.Named

class UpdateGratifNotification @Inject constructor(@Named(UPDATE_GRATIF) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {
    private val PARAMS = UpdataGratifNotificationParams

    suspend fun getResponse(map: HashMap<String, Any>): UpdateGratificationNotificationResponse{
        return gqlWrapper.getResponse(UpdateGratificationNotificationResponse::class.java, queryString, map)
    }

    fun getQueryParams(notificationId: Int, notificationEntryType:Int, popType:Int, screenName:String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.NOTIFICATION_ID] = notificationId
        variables[PARAMS.NOTIFICATION_ENTRY_TYPE] = notificationEntryType
        variables[PARAMS.POP_UP_TYPE] = popType
        variables[PARAMS.SCREEN_NAME] = screenName
        return variables
    }
}