package com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class WebSocketCsatResponse(

	@SerializedName("is_opposite")
	val isOpposite: Boolean? = null,

	@SerializedName("thumbnail")
	val thumbnail: String? = null,

	@SerializedName("from_role")
	val fromRole: String? = null,

	@SerializedName("message")
	val message: Message? = null,

	@SerializedName("from_uid")
	val fromUid: Int? = null,

	@SerializedName("show_rating")
	val showRating: Boolean? = null,

	@SerializedName("start_time")
	val startTime: String? = null,

	@SerializedName("from_user_name")
	val fromUserName: String? = null,

	@SerializedName("rating_status")
	val ratingStatus: Int? = null,

	@SerializedName("attachment")
	val attachment: Attachment? = null,

	@SerializedName("to_uid")
	val toUid: Int? = null,

	@SerializedName("attachment_id")
	val attachmentId: Int? = null,

	@SerializedName("from")
	val from: String? = null,

	@SerializedName("to_buyer")
	val toBuyer: Boolean? = null,

	@SerializedName("msg_id")
	val msgId: Long? = null,

	@SerializedName("is_bot")
	val isBot: Boolean? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readString(),
			parcel.readString(),
			parcel.readParcelable(Message::class.java.classLoader),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readParcelable(Attachment::class.java.classLoader),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readValue(Long::class.java.classLoader) as? Long,
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(isOpposite)
		parcel.writeString(thumbnail)
		parcel.writeString(fromRole)
		parcel.writeParcelable(message, flags)
		parcel.writeValue(fromUid)
		parcel.writeValue(showRating)
		parcel.writeString(startTime)
		parcel.writeString(fromUserName)
		parcel.writeValue(ratingStatus)
		parcel.writeParcelable(attachment, flags)
		parcel.writeValue(toUid)
		parcel.writeValue(attachmentId)
		parcel.writeString(from)
		parcel.writeValue(toBuyer)
		parcel.writeValue(msgId)
		parcel.writeValue(isBot)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<WebSocketCsatResponse> {
		override fun createFromParcel(parcel: Parcel): WebSocketCsatResponse {
			return WebSocketCsatResponse(parcel)
		}

		override fun newArray(size: Int): Array<WebSocketCsatResponse?> {
			return arrayOfNulls(size)
		}
	}
}