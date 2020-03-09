package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use
import android.os.Parcelable
import android.os.Parcel

data class AdditionalInfoUiModel(
		var messageInfoUiModel: MessageInfoUiModel = MessageInfoUiModel(),
		var errorDetailUiModel: ErrorDetailUiModel = ErrorDetailUiModel()
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readParcelable(MessageInfoUiModel::class.java.classLoader),
			parcel.readParcelable(ErrorDetailUiModel::class.java.classLoader)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(messageInfoUiModel, flags)
		parcel.writeParcelable(errorDetailUiModel, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<AdditionalInfoUiModel> {
		override fun createFromParcel(parcel: Parcel): AdditionalInfoUiModel {
			return AdditionalInfoUiModel(parcel)
		}

		override fun newArray(size: Int): Array<AdditionalInfoUiModel?> {
			return arrayOfNulls(size)
		}
	}
}
