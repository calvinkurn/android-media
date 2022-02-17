package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * @author lalit.singh
 */
data class AmplificationCMInApp (
    @Expose
    @SerializedName("notificationId")
    var id: Long? = 0,

    @Expose
    @SerializedName("campaignId")
    var campaignId: String? = null,

    @Expose
    @SerializedName(value = "freq")
    var freq: Int? = 1,

    @Expose
    @SerializedName("notificationType")
    var type: String? = "",

    @Expose
    @SerializedName("campaignUserToken")
    var campaignUserToken: String? = "",

    @Expose
    @SerializedName("parentId")
    var parentId: String? = null,

    @Expose
    @SerializedName("e")
    var expiry: Long? = 0,

    @Expose
    @SerializedName("s")
    var screen: String? = "*",

    @Expose
    @SerializedName("ss")
    var ss: String? = "",

    @Expose
    @SerializedName("campaignCode")
    var campaignCode: String? = "",

    @Expose
    @SerializedName(value = "d")
    var isCancelable: Boolean? = false,

    @Expose
    @SerializedName("ui")
    var cmLayout: CMLayout? = CMLayout(),

    @Expose
    @SerializedName(value = "st")
    var startTime: Long? = 0,

    @Expose
    @SerializedName(value = "et")
    var endTime: Long? = 0,

    @Expose
    @SerializedName(value = "is_test")
    var isTest: Boolean? = false,

    @Expose
    @SerializedName(value = "perst_on")
    var isPersistentToggle: Boolean? = true,

    @Expose
    @SerializedName(value = "customValues")
    var customValues: String? = "",

    @Expose
    @SerializedName(CMConstant.PayloadKeys.SHOP_ID)
    var shopId: String? = null,

    @Expose
    @SerializedName(CMConstant.PayloadKeys.PayloadExtraDataKey.CAMPAIGN_NAME)
    var campaignName: String?,

    @Expose
    @SerializedName(CMConstant.PayloadKeys.PayloadExtraDataKey.JOURNEY_ID)
    var journeyId: String?,

    @Expose
    @SerializedName(CMConstant.PayloadKeys.PayloadExtraDataKey.JOURNEY_NAME)
    var journeyName: String?,

    @Expose
    @SerializedName(CMConstant.PayloadKeys.PayloadExtraDataKey.SESSION_ID)
    var sessionId: String?,


)