package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo

data class GroupShop(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("shop_shipments")
        val shopShipments: List<ShopShipment> = emptyList(),
        @SerializedName("products")
        val products: List<Product> = emptyList(),
        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        val shippingId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        val spId: Int = 0,
        @SerializedName("dropshipper")
        val dropshiper: Dropshiper = Dropshiper(),
        @SerializedName("is_insurance")
        val isInsurance: Boolean = false,
        @SerializedName("is_fulfillment_service")
        val isFulfillment: Boolean = false,
        @SerializedName("toko_cabang")
        val tokoCabangInfo: TokoCabangInfo = TokoCabangInfo(),
        @SerializedName("warehouse")
        val warehouse: Warehouse = Warehouse(),
        @SerializedName("cart_string")
        val cartString: String = "",
        @SerializedName("has_promo_list")
        val isHasPromoList: Boolean = false,
        @SerializedName("save_state_flag")
        val isSaveStateFlag: Boolean = false,
        @SerializedName("vehicle_leasing")
        val vehicleLeasing: VehicleLeasing = VehicleLeasing(),
        @SerializedName("promo_codes")
        val listPromoCodes: List<String> = emptyList(),
        @SerializedName("shipment_information")
        val shipmentInformation: ShipmentInformation = ShipmentInformation(),
        @SerializedName("is_disable_change_courier")
        val isDisableChangeCourier: Boolean = false,
        @SerializedName("bo_metadata")
        val boMetadata: BoMetadata = BoMetadata()
)