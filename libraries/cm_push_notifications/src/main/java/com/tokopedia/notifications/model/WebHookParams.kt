package com.tokopedia.notifications.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.CommonUtils.fromJson
import com.tokopedia.graphql.CommonUtils.toJson
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.NOTIFCENTER_NOTIFICATION_ID
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.NOTIFCENTER_NOTIFICATION_TYPE
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.WEBHOOK_PARAM

data class WebHookParams(
    @Expose
    @SerializedName(WEBHOOK_PARAM)
    var webHookParams: Data? = null
) {
    data class Data(
        @Expose
        @SerializedName(NOTIFCENTER_NOTIFICATION_ID)
        val notificationCenterId: String? = "",

        @Expose
        @SerializedName(NOTIFCENTER_NOTIFICATION_TYPE)
        val notificationCenterTypeOf: Int? = 0,

        @Expose
        @SerializedName(NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY)
        val notificationTemplateKey: String? = null
    )

    companion object {
        private fun webHook(json: String): WebHookParams {
            val webHookParamData = getWebHookData(json)
            return WebHookParams().apply {
                this.webHookParams = webHookParamData
            }
        }

        fun getWebHookData(json: String?): Data {
            return try {
                if (!json.isNullOrBlank()) {
                    fromJson(json, Data::class.java)
                } else {
                    Data()
                }
            } catch (ignored: Throwable) {
                Data()
            }
        }

        fun webHookToJson(json: String?): String {
            if (json == null) return ""
            return toJson(webHook(json))
        }
    }
}
