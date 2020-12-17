package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel


data class UpdateAndValidateUseData(
        var updateCartData: UpdateCartData? = null,
        var promoUiModel: PromoUiModel? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(UpdateCartData::class.java.classLoader),
            parcel.readParcelable(PromoUiModel::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(updateCartData, flags)
        parcel.writeParcelable(promoUiModel, flags)
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
