package com.tokopedia.editshipping.domain.model.shippingEditor

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ShippingEditorVisitable

data class ShipperListModel(
        var shippers: ShippersModel = ShippersModel(),
        var ticker: List<TickerModel> = emptyList()
)

data class ShippersModel(
        var onDemand: List<OnDemandModel> = emptyList(),
        var conventional: List<ConventionalModel> = emptyList()
)

data class OnDemandModel(
        var shipperId: Int = -1,
        var shipperName: String = "",
        var isActive: Boolean = false,
        var textPromo: String = "",
        var image: String = "",
        var featureInfo: List<FeatureInfoModel> = listOf(),
        var shipperProduct: List<ShipperProductModel> = listOf(),
        var tickerState: Int = 0,
        var isAvailable: Boolean = true,
        var warehouseModel: List<WarehousesModel> = listOf(),
        var listActivatedSpId: MutableSet<String> = mutableSetOf()
) : ShippingEditorVisitable()

data class ConventionalModel(
        var shipperId: Int = -1,
        var shipperName: String = "",
        var isActive: Boolean = false,
        var textPromo: String = "",
        var image: String = "",
        var featureInfo: List<FeatureInfoModel> = listOf(),
        var shipperProduct: List<ShipperProductModel> = listOf(),
        var tickerState: Int = 0,
        var isAvailable: Boolean = true,
        var warehouseModel: List<WarehousesModel> = listOf(),
        var listActivatedSpId: MutableSet<String> = mutableSetOf()
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
        var isActive: Boolean = false
) : Parcelable

data class TickerModel(
        var header: String = "",
        var body: String = "",
        var textLink: String = "",
        var urlLink: String = ""
)
