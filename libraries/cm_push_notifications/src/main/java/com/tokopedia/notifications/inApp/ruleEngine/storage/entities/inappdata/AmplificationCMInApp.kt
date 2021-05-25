package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author lalit.singh
 */
data class AmplificationCMInApp (
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("notificationId")
    var id: Long? = 0,

    @Expose
    @ColumnInfo(name = "campaignId")
    @SerializedName("campaignId")
    var campaignId: Long? = null,

    @Expose
    @ColumnInfo(name = "freq")
    @SerializedName(value = "freq")
    var freq: Int? = 1,

    @Expose
    @ColumnInfo(name = "notificationType")
    @SerializedName("notificationType")
    var type: String? = "",

    @Expose
    @ColumnInfo(name = "campaignUserToken")
    @SerializedName("campaignUserToken")
    var campaignUserToken: String? = "",

    @Expose
    @ColumnInfo(name = "parentId")
    @SerializedName("parentId")
    var parentId: Long? = null,

    @Expose
    @ColumnInfo(name = "e")
    @SerializedName("e")
    var expiry: Long? = 0,

    @Expose
    @ColumnInfo(name = "inAnim")
    @SerializedName("inAnim")
    var inAnim: String? = "",

    @Expose
    @ColumnInfo(name = "s")
    @SerializedName("s")
    var screen: String? = "*",

    @Expose
    @Ignore
    @SerializedName("ss")
    var ss: String? = "",

    @Expose
    @ColumnInfo(name = "campaignCode")
    @SerializedName("campaignCode")
    var campaignCode: String? = "",

    @Expose
    @SerializedName("d")
    @ColumnInfo(name = "d")
    var isCancelable: Boolean? = false,

    @Expose
    @SerializedName("ui")
    @Embedded(prefix = "ui_")
    var cmLayout: CMLayout? = CMLayout(),

    @Expose
    @ColumnInfo(name = "st")
    @SerializedName(value = "st")
    var startTime: Long? = 0,

    @Expose
    @SerializedName(value = "et")
    @ColumnInfo(name = "et")
    var endTime: Long? = 0,

    @Expose
    @SerializedName(value = "ct")
    @ColumnInfo(name = "ct")
    var currentTime: Long? = 0,

    @Expose
    @SerializedName(value = "buf_time")
    @ColumnInfo(name = "buf_time")
    var bufTime: Long? = 0,

    @ColumnInfo(name = "shown")
    var isShown: Boolean? = false,

    @ColumnInfo(name = "last_shown")
    var lastShownTime: Long? = 0,

    @ColumnInfo(name = "is_test")
    var isTest: Boolean? = false,

    @ColumnInfo(name = "perst_on")
    var isPersistentToggle: Boolean? = true,

    @ColumnInfo(name = "is_interacted")
    var isInteracted: Boolean? = false,

    @ColumnInfo(name = "is_amplification")
    var isAmplification: Boolean? = false,

    @ColumnInfo(name = "customValues")
    var customValues: String? = ""
)