package com.tokopedia.checkout.bundle.data.model.request.checkout

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.bundle.view.uimodel.CrossSellModel
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentCrossSellModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CrossSellRequest(
        @SerializedName("main_vertical_id")
        var mainVerticalId: Int = 1,
        @SerializedName("items")
        var listItem: ArrayList<ShipmentCrossSellModel> = arrayListOf()
) : Parcelable
