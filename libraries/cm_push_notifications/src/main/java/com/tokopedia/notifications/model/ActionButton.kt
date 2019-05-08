package com.tokopedia.notifications.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */
data class ActionButton(
        @SerializedName(CMConstant.PayloadKeys.TEXT)
        var text: String? = null,

        @SerializedName(CMConstant.PayloadKeys.APP_LINK)
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.ACTION_BUTTON_ICON)
        var actionButtonIcon: String? = null
)
