package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.AdditionalInfoUiModel
import android.os.Parcel
import android.os.Parcelable


data class UpdateAndValidateUseData(
        var updateCartData: UpdateCartData? = null,
        var additionalInfoUiModel: AdditionalInfoUiModel? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(UpdateCartData::class.java.classLoader),
            parcel.readParcelable(AdditionalInfoUiModel::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(updateCartData, flags)
        parcel.writeParcelable(additionalInfoUiModel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UpdateAndValidateUseData> {
        override fun createFromParcel(parcel: Parcel): UpdateAndValidateUseData {
            return UpdateAndValidateUseData(parcel)
        }

        override fun newArray(size: Int): Array<UpdateAndValidateUseData?> {
            return arrayOfNulls(size)
        }
    }

}
