package com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Attributes(

    @SerializedName("show_other_reason")
    val showOtherReason: Boolean? = null,

    @SerializedName("reasons")
    val reasons: List<String?>? = null,

    @SerializedName("reason_title")
    val reasonTitle: String? = null,

    @SerializedName("fallback_attachment")
    val fallbackAttachment: FallbackAttachment? = null,

    @SerializedName("chatbot_session_id")
    val chatbotSessionId: String? = null,

    @SerializedName("livechat_session_id")
    val livechatSessionId: String? = null,

    @SerializedName("trigger_rule_type")
    val triggerRuleType: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("points")
    val points: List<PointsItem?>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readParcelable(FallbackAttachment::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(PointsItem)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(showOtherReason)
        parcel.writeStringList(reasons)
        parcel.writeString(reasonTitle)
        parcel.writeParcelable(fallbackAttachment, flags)
        parcel.writeString(chatbotSessionId)
        parcel.writeString(livechatSessionId)
        parcel.writeString(triggerRuleType)
        parcel.writeString(title)
        parcel.writeTypedList(points)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attributes> {
        override fun createFromParcel(parcel: Parcel): Attributes {
            return Attributes(parcel)
        }

        override fun newArray(size: Int): Array<Attributes?> {
            return arrayOfNulls(size)
        }
    }
}
