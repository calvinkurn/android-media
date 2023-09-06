package com.tokopedia.editshipping.domain.model.shippingEditor

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ShippingEditorVisitable

data class ShipperListModel(
    var shippers: ShipperGroupModel = ShipperGroupModel(),
    var ticker: List<TickerModel> = emptyList(),
    var dropOffMapsUrl: String = ""
)

data class ShipperGroupModel(
    var onDemand: List<ShipperModel> = emptyList(),
    var conventional: List<ShipperModel> = emptyList()
)

data class ShipperModel(
    var shipperId: Long = -1,
    var shipperName: String = "",
    var isActive: Boolean = false,
    var textPromo: String = "",
    var image: String = "",
    var featureInfo: List<FeatureInfoModel> = listOf(),
    var shipperProduct: List<ShipperProductModel> = listOf(),
    var tickerState: Int = 0,
    var isAvailable: Boolean = true,
    var warehouseModel: List<WarehousesModel> = listOf(),
    var listActivatedSpId: MutableSet<String> = mutableSetOf(),
    var isWhitelabel: Boolean = false,
    var description: String = ""
) : ShippingEditorVisitable()

@Parcelize
data class FeatureInfoModel(
    var header: String = "",
    var body: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(header)
        parcel.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeatureInfoModel> {
        override fun createFromParcel(parcel: Parcel): FeatureInfoModel {
            return FeatureInfoModel(parcel)
        }

        override fun newArray(size: Int): Array<FeatureInfoModel?> {
            return arrayOfNulls(size)
        }
    }
}

@Parcelize
data class ShipperProductModel(
    var shipperProductId: String = "",
    var shipperProductName: String = "",
    var isActive: Boolean = false,
    var isAvailable: Boolean = true
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shipperProductId)
        parcel.writeString(shipperProductName)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeByte(if (isAvailable) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShipperProductModel> {
        override fun createFromParcel(parcel: Parcel): ShipperProductModel {
            return ShipperProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ShipperProductModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class TickerModel(
    var header: String = "",
    var body: String = "",
    var textLink: String = "",
    var urlLink: String = ""
)
