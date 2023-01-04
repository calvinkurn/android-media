package com.tokopedia.editshipping.domain.model.shippingEditor

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ShippingEditorVisitable

data class ShipperListModel(
    var shippers: ShipperGroupModel = ShipperGroupModel(),
    var ticker: List<TickerModel> = emptyList()
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
) : Parcelable

@Parcelize
data class ShipperProductModel(
    var shipperProductId: String = "",
    var shipperProductName: String = "",
    var isActive: Boolean = false,
    var isAvailable: Boolean = true
) : Parcelable

data class TickerModel(
    var header: String = "",
    var body: String = "",
    var textLink: String = "",
    var urlLink: String = ""
)
