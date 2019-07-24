package com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Attachment(

	@SerializedName("fallback_attachment")
	val fallbackAttachment: FallbackAttachment? = null,

	@SerializedName("attributes")
	val attributes: Attributes? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("type")
	val type: Int? = null
):Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readParcelable(FallbackAttachment::class.java.classLoader),
			parcel.readParcelable(Attributes::class.java.classLoader),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Int::class.java.classLoader) as? Int) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(fallbackAttachment, flags)
		parcel.writeParcelable(attributes, flags)
		parcel.writeValue(id)
		parcel.writeValue(type)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Attachment> {
		override fun createFromParcel(parcel: Parcel): Attachment {
			return Attachment(parcel)
		}

		override fun newArray(size: Int): Array<Attachment?> {
			return arrayOfNulls(size)
		}
	}
}