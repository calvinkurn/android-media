package com.tokopedia.notifcenter.view.customview

import com.tokopedia.topads.sdk.utils.*

object NotificationTopAdsHeadlineHelper {
    private const val PARAM_NOTIFICATION_CENTER = "notificationcenter"
    fun getParams(userId: String): String {
        return UrlParamHelper.generateUrlParamString(
            mapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_PAGE to 0,
                PARAM_EP to VALUE_EP,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to PARAM_NOTIFICATION_CENTER,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userId
            )
        )
    }
}
