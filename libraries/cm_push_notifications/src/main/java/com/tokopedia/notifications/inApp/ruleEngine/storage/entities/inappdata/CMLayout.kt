package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant
import java.util.ArrayList

data class CMLayout(
        @Expose
        @Embedded(prefix = "bg_")
        @SerializedName("bg")
        val foreground: CMBackground? = CMBackground(),

        @Expose
        @Embedded(prefix = "ttl_")
        @SerializedName("ttl")
        val titleText: CMText? = CMText(),

        @Expose
        @Embedded(prefix = "msg_")
        @SerializedName("msg")
        val messageText: CMText? = CMText(),

        @Expose
        @ColumnInfo(name = "img")
        @SerializedName("img")
        var img: String = "",

        @Expose
        @SerializedName("appLink")
        @ColumnInfo(name = "appLink")
        val appLink: String = "",

        @Expose
        @ColumnInfo(name = "btnOri")
        @SerializedName("btnOri")
        val btnOrientation: String = CmInAppConstant.ORIENTATION_VERTICAL,

        @Expose
        @ColumnInfo(name = "inAppButtons")
        @SerializedName("inAppButtons")
        val button: ArrayList<CMButton>? = arrayListOf()
)
