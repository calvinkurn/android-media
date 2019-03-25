package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import android.os.Parcel
import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Message(

	@field:SerializedName("color")
	val color: String? = "",

	@field:SerializedName("state")
	val state: String? = "",

	@field:SerializedName("text")
	val text: String? = ""
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(color)
		parcel.writeString(state)
		parcel.writeString(text)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Message> {
		override fun createFromParcel(parcel: Parcel): Message {
			return Message(parcel)
		}

		override fun newArray(size: Int): Array<Message?> {
			return arrayOfNulls(size)
		}
	}
}