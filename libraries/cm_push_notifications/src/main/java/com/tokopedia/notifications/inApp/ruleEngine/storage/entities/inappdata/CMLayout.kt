package com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata

import androidx.room.Embedded
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant

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
        @Embedded(prefix = "img")
        @SerializedName("img")
        val img: String = "",

        @Expose
        @Embedded(prefix = "appLink")
        @SerializedName("appLink")
        val appLink: String = "",

        @Expose
        @Embedded(prefix = "btnOri")
        @SerializedName("btnOri")
        val btnOrientation: String = CmInAppConstant.ORIENTATION_VERTICAL,

        @Expose
        @Embedded(prefix = "inAppButtons")
        @SerializedName("inAppButtons")
        val button: List<CMButton>? = emptyList()
)