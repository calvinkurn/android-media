package com.tokopedia.checkout.data.model.request.checkout

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero

/**
 * Since original class CheckoutRequest contains many unnecessary data, which is currently used for analytics purpose,
 * and we need to send data to backend and we should not send unnecessary data to backend,
 * we decided to create new `clean` class to wrap checkout request data
 *
 * @see CheckoutRequest() <-- Original class
 */
data class CheckoutRequestGqlData(
        @SerializedName("promos")
        var promos: List<PromosGqlData> = emptyList(),
        @SerializedName("promo_code")
        var promoCode: String? = null,
        @SerializedName("promo_codes")
        var promoCodes: List<String> = emptyList(),
        @SerializedName("is_donation")
        var isDonation: Int = 0,
        @SerializedName("egold_data")
        var egoldData: EgoldGqlData? = null,
        @SerializedName("data")
        var data: List<CheckoutGqlData> = emptyList(),
        @SerializedName("tokopedia_corner_data")
        var tokopediaCornerData: TokopediaCornerGqlData? = null,
        @SerializedName("has_promo_stacking")
        var hasPromoStacking: Boolean = false,
        @SerializedName("leasing_id")
        @SuppressLint("Invalid Data Type")
        var leasingId: Int = 0
)

data class EgoldGqlData(
        @SerializedName("is_egold")
        var isEgold: Boolean = false,
        @SerializedName("gold_amount")
        var egoldAmount: Long = 0
)

data class TokopediaCornerGqlData(
        @SerializedName("is_tokopedia_corner")
        var isTokopediaCorner: Boolean = false,
        @SerializedName("user_corner_id")
        var userCornerId: String? = null,
        @SerializedName("corner_id")
        @SuppressLint("Invalid Data Type")
        var cornerId: Long = 0
)

data class CheckoutGqlData(
        @SerializedName("address_id")
        @SuppressLint("Invalid Data Type")
        var addressId: Long = 0,
        @SerializedName("shop_products")
        var shopProducts: List<ShopProductGqlData> = ArrayList()
)

data class ShopProductGqlData(
        @SerializedName("shop_id")
        @SuppressLint("Invalid Data Type")
        var shopId: Long = 0,
        @SerializedName("is_preorder")
        var isPreorder: Int = 0,
        @SerializedName("finsurance")
        var finsurance: Int = 0,
        @SerializedName("shipping_info")
        var shippingInfo: ShippingInfoGqlData? = null,
        @SerializedName("is_dropship")
        var isDropship: Int = 0,
        @SerializedName("dropship_data")
        var dropshipData: DropshipGqlData? = null,
        @SerializedName("product_data")
        var productData: List<ProductGqlData> = emptyList(),
        @SerializedName("fcancel_partial")
        var fcancelPartial: Int = 0,
        @SerializedName("warehouse_id")
        @SuppressLint("Invalid Data Type")
        var warehouseId: Long = 0,
        @SerializedName("promo_codes")
        var promoCodes: List<String> = emptyList(),
        @SerializedName("promos")
        var promos: List<PromosGqlData> = emptyList(),
        @SerializedName("is_order_priority")
        var isOrderPriority: Int = 0
)

data class ShippingInfoGqlData(
        @SerializedName("shipping_id")
        @SuppressLint("Invalid Data Type")
        var shippingId: Int = 0,
        @SerializedName("sp_id")
        @SuppressLint("Invalid Data Type")
        var spId: Int = 0,
        @SerializedName("rates_id")
        var ratesId: String? = null,
        @SerializedName("checksum")
        var checksum: String? = null,
        @SerializedName("ut")
        var ut: String? = null,
        @SerializedName("rates_feature")
        var ratesFeature: RatesFeatureGqlData? = null
)

data class RatesFeatureGqlData(
        @SerializedName("ontime_delivery_guarantee")
        var ontimeDeliveryGuarantee: OntimeDeliveryGuaranteeGqlData? = null
)

data class OntimeDeliveryGuaranteeGqlData(
        @SerializedName("available")
        var available: Boolean = false,
        @SerializedName("duration")
        var duration: Int = 0
)

data class DropshipGqlData(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("telp_no")
        var telpNo: String? = null
)

data class ProductGqlData(
        @SerializedName("product_id")
        @SuppressLint("Invalid Data Type")
        var productId: Long = 0,
        @SerializedName("is_ppp")
        var isPurchaseProtection: Boolean = false,
        @SerializedName("product_quantity")
        var productQuantity: Int = 0,
        @SerializedName("product_notes")
        var productNotes: String? = null
)

data class PromosGqlData(
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("code")
        var code: String? = null
)

object CheckoutRequestGqlDataMapper {

    fun map(checkoutRequest: CheckoutRequest): CheckoutRequestGqlData {
        return CheckoutRequestGqlData().apply {
            val tmpPromos = checkoutRequest.promos
            promos = if (tmpPromos != null) mapPromos(tmpPromos) else emptyList()
            promoCode = checkoutRequest.promoCode
            promoCodes = checkoutRequest.promoCodes ?: emptyList()
            isDonation = checkoutRequest.isDonation
            val tmpEgoldData = checkoutRequest.egoldData
            egoldData = if (tmpEgoldData != null) mapEgoldData(tmpEgoldData) else null
            val tmpCheckoutRequestData = checkoutRequest.data
            data = if (tmpCheckoutRequestData != null) mapData(tmpCheckoutRequestData) else emptyList()
            val tmpCornerData = checkoutRequest.cornerData
            tokopediaCornerData = if (tmpCornerData != null) mapTokopediaCornerData(tmpCornerData) else null
            hasPromoStacking = checkoutRequest.hasPromoStacking
            leasingId = checkoutRequest.leasingId
        }
    }

    private fun mapPromos(promos: List<PromoRequest>): List<PromosGqlData> {
        val promosGqlData = ArrayList<PromosGqlData>()
        promos.forEach {
            promosGqlData.add(PromosGqlData().apply {
                type = it.type
                code = it.code
            })
        }

        return promosGqlData
    }

    private fun mapEgoldData(egoldData: EgoldData): EgoldGqlData {
        return EgoldGqlData().apply {
            isEgold = egoldData.isEgold
            egoldAmount = egoldData.egoldAmount
        }
    }

    private fun mapTokopediaCornerData(tokopediaCornerData: TokopediaCornerData): TokopediaCornerGqlData {
        return TokopediaCornerGqlData().apply {
            isTokopediaCorner = tokopediaCornerData.isTokopediaCorner
            userCornerId = tokopediaCornerData.userCornerId
            cornerId = tokopediaCornerData.cornerId
        }
    }

    private fun mapData(dataCheckoutRequestList: List<DataCheckoutRequest>): List<CheckoutGqlData> {
        val checkoutGqlDataList = ArrayList<CheckoutGqlData>()
        dataCheckoutRequestList.forEach {
            checkoutGqlDataList.add(CheckoutGqlData().apply {
                addressId = it.addressId.toLongOrZero()
                val tmpShopProducts = it.shopProducts
                shopProducts = if (tmpShopProducts != null) mapShopProduct(tmpShopProducts) else emptyList()
            })
        }

        return checkoutGqlDataList
    }

    private fun mapShopProduct(shopProductCheckoutRequests: List<ShopProductCheckoutRequest>): List<ShopProductGqlData> {
        val shopProductList = ArrayList<ShopProductGqlData>()
        shopProductCheckoutRequests.forEach {
            shopProductList.add(ShopProductGqlData().apply {
                shopId = it.shopId
                isPreorder = it.isPreorder
                finsurance = it.finsurance
                val tmpShippingInfo = it.shippingInfo
                shippingInfo = if (tmpShippingInfo != null) mapShippingInfo(tmpShippingInfo) else null
                isDropship = it.isDropship
                val tmpDropshipData = it.dropshipData
                dropshipData = if (tmpDropshipData != null) mapDropshipData(tmpDropshipData) else null
                val tmpProductData = it.productData
                productData = if (tmpProductData != null) mapProductData(tmpProductData) else emptyList()
                fcancelPartial = it.fcancelPartial
                warehouseId = it.warehouseId
                promoCodes = it.promoCodes ?: emptyList()
                val tmpPromos = it.promos
                promos = if (tmpPromos != null) mapPromos(tmpPromos) else emptyList()
                isOrderPriority = it.isOrderPriority
            })
        }

        return shopProductList
    }

    private fun mapProductData(productDataList: List<ProductDataCheckoutRequest>): List<ProductGqlData> {
        val productGqldataList = ArrayList<ProductGqlData>()
        productDataList.forEach {
            productGqldataList.add(ProductGqlData().apply {
                productId = it.productId
                isPurchaseProtection = it.isPurchaseProtection
                productQuantity = it.productQuantity
                productNotes = it.productNotes
            })
        }

        return productGqldataList
    }

    private fun mapShippingInfo(shippingInfo: ShippingInfoCheckoutRequest): ShippingInfoGqlData {
        return ShippingInfoGqlData().apply {
            shippingId = shippingInfo.shippingId
            spId = shippingInfo.spId
            ratesId = shippingInfo.ratesId
            checksum = shippingInfo.checksum
            ut = shippingInfo.ut
            val tmpRatesFeature = shippingInfo.ratesFeature
            ratesFeature = if (tmpRatesFeature != null) mapRatesFeature(tmpRatesFeature) else null
        }
    }

    private fun mapRatesFeature(ratesFeature: com.tokopedia.checkout.data.model.request.common.RatesFeature): RatesFeatureGqlData {
        return RatesFeatureGqlData().apply {
            ontimeDeliveryGuarantee =
                    if (ratesFeature.ontimeDeliveryGuarantee != null) {
                        OntimeDeliveryGuaranteeGqlData().apply {
                            available = ratesFeature.ontimeDeliveryGuarantee?.available ?: false
                            duration = ratesFeature.ontimeDeliveryGuarantee?.duration ?: 0
                        }
                    } else null
        }
    }

    private fun mapDropshipData(dropshipData: DropshipDataCheckoutRequest): DropshipGqlData {
        return DropshipGqlData().apply {
            name = dropshipData.name
            telpNo = dropshipData.telpNo
        }
    }

}