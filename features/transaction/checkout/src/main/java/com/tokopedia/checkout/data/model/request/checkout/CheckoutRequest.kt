package com.tokopedia.checkout.data.model.request.checkout

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.data.model.request.checkout.OrderMetadata.Companion.FREE_SHIPPING_METADATA
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellRequest
import com.tokopedia.checkout.data.model.request.checkout.old.AddOnGiftingRequest
import com.tokopedia.checkout.data.model.request.checkout.old.CheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.DropshipDataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.EgoldData
import com.tokopedia.checkout.data.model.request.checkout.old.ProductDataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.PromoRequest
import com.tokopedia.checkout.data.model.request.checkout.old.ShippingInfoCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.ShopProductCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.TokopediaCornerData
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero

const val FEATURE_TYPE_REGULAR_PRODUCT = 3
const val FEATURE_TYPE_TOKONOW_PRODUCT = 12
const val UPLOAD_PRESCRIPTION_META_DATA_KEY = "prescription_ids"
const val SCHEDULE_DELIVERY_META_DATA_KEY = "shipping_validation_metadata"
data class Carts(
    @SerializedName("has_promo_stacking")
    var hasPromoStacking: Boolean = false,
    @SerializedName("is_donation")
    var isDonation: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("leasing_id")
    var leasingId: Long = 0,
    @SerializedName("promos")
    var promos: List<Promo> = emptyList(),
    @SerializedName("egold_data")
    var egold: Egold = Egold(),
    @SerializedName("tokopedia_corner_data")
    var tokopediaCorner: TokopediaCorner? = null,
    @SerializedName("feature_type")
    var featureType: Int = 0,
    @SerializedName("cross_sell")
    var crossSell: CrossSellRequest? = null,

    @SerializedName("data")
    var data: List<Data> = emptyList()
)

data class Data(
    @SuppressLint("Invalid Data Type")
    @SerializedName("address_id")
    var addressId: Long = 0,
    @SerializedName("shop_orders")
    var shopOrders: List<ShopOrder> = emptyList()
)

data class ShopOrder(
    @SerializedName("bundle")
    var bundle: List<Bundle> = emptyList(),
    @SerializedName("cartstring")
    var cartstring: String = "",
    @SerializedName("dropship_data")
    var dropship: Dropship = Dropship(),
    @SerializedName("is_preorder")
    var isPreorder: Int = 0,
    @SerializedName("order_feature")
    var orderFeature: OrderFeature = OrderFeature(),
    @SerializedName("promos")
    var promos: List<Promo> = emptyList(),
    @SerializedName("shipping_info")
    var shippingInfo: ShippingInfo = ShippingInfo(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("shop_id")
    var shopId: Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("warehouse_id")
    var warehouseId: Long = 0,
    @SerializedName("items")
    var checkoutGiftingOrderLevel: List<CheckoutGiftingAddOn> = emptyList(),
    @SerializedName("order_metadata")
    var orderMetadata: List<OrderMetadata> = emptyList()
)

data class Bundle(
    @SerializedName("bundle_info")
    var bundleInfo: BundleInfo = BundleInfo(),
    @SerializedName("product_data")
    var productData: List<Product> = emptyList()
)

data class Product(
    @SerializedName("is_ppp")
    var isPpp: Boolean = false,
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("items")
    var checkoutGiftingProductLevel: List<CheckoutGiftingAddOn> = emptyList()
)

data class CheckoutGiftingAddOn(
    @SerializedName("item_type")
    var itemType: String = "",
    @SerializedName("item_id")
    var itemId: String = "",
    @SerializedName("item_qty")
    var itemQty: Int = 0,
    @SerializedName("item_metadata")
    var itemMetadata: String = ""
)

data class BundleInfo(
    @SerializedName("bundle_group_id")
    var bundleGroupId: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("bundle_id")
    var bundleId: Long = 0
)

data class Dropship(
    @SerializedName("is_dropship")
    var isDropship: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("telp_no")
    var telpNo: String = ""
)

data class OrderFeature(
    @SerializedName("is_order_priority")
    var isOrderPriority: Int = 0
)

data class ShippingInfo(
    @SerializedName("checksum")
    var checksum: String = "",
    @SerializedName("finsurance")
    var finsurance: Int = 0,
    @SerializedName("rates_feature")
    var ratesFeature: RatesFeature = RatesFeature(),
    @SerializedName("rates_id")
    var ratesId: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("shipping_id")
    var shippingId: Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("sp_id")
    var spId: Long = 0,
    @SerializedName("ut")
    var ut: String = ""
)

data class RatesFeature(
    @SerializedName("ontime_delivery_guarantee")
    var ontimeDeliveryGuarantee: OntimeDeliveryGuarantee = OntimeDeliveryGuarantee()
)

data class OntimeDeliveryGuarantee(
    @SerializedName("available")
    var available: Boolean = false,
    @SerializedName("duration")
    var duration: Int = 0
)

data class Egold(
    @SerializedName("gold_amount")
    var goldAmount: Long = 0,
    @SerializedName("is_egold")
    var isEgold: Boolean = false
)

data class Promo(
    @SerializedName("code")
    var code: String = "",
    @SerializedName("type")
    var type: String = ""
)

data class TokopediaCorner(
    @SerializedName("is_tokopedia_corner")
    var isTokopediaCorner: Boolean = false,
    @SerializedName("user_corner_id")
    var userCornerId: String = "",
    @SerializedName("corner_id")
    @SuppressLint("Invalid Data Type")
    var cornerId: Long = 0
)

data class OrderMetadata(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = ""
) {
    companion object {
        const val FREE_SHIPPING_METADATA = "free_shipping_metadata"
    }
}

object CheckoutRequestMapper {

    fun map(checkoutRequest: CheckoutRequest): Carts {
        return Carts().apply {
            promos = mapPromos(checkoutRequest.promos)
            isDonation = checkoutRequest.isDonation
            egold = mapEgoldData(checkoutRequest.egoldData)
            data = mapData(checkoutRequest.data, checkoutRequest.prescriptionIds)
            val tmpCornerData = checkoutRequest.cornerData
            tokopediaCorner = if (tmpCornerData != null) mapTokopediaCornerData(tmpCornerData) else null
            hasPromoStacking = checkoutRequest.hasPromoStacking
            leasingId = checkoutRequest.leasingId
            featureType = checkoutRequest.featureType
            crossSell = checkoutRequest.crossSell
        }
    }

    private fun mapPromos(promos: List<PromoRequest>?): List<Promo> {
        val promosGqlData = mutableListOf<Promo>()
        promos?.forEach {
            promosGqlData.add(
                Promo().apply {
                    type = it.type
                    code = it.code
                }
            )
        }

        return promosGqlData
    }

    private fun mapEgoldData(egoldData: EgoldData?): Egold {
        return Egold().apply {
            isEgold = egoldData?.isEgold ?: false
            goldAmount = egoldData?.egoldAmount ?: 0
        }
    }

    private fun mapTokopediaCornerData(tokopediaCornerData: TokopediaCornerData): TokopediaCorner {
        return TokopediaCorner().apply {
            isTokopediaCorner = tokopediaCornerData.isTokopediaCorner
            userCornerId = tokopediaCornerData.userCornerId ?: "0"
            cornerId = tokopediaCornerData.cornerId
        }
    }

    private fun mapData(
        dataCheckoutRequestList: List<DataCheckoutRequest>?,
        prescriptionIds: ArrayList<String>?
    ): List<Data> {
        val checkoutGqlDataList = mutableListOf<Data>()
        dataCheckoutRequestList?.forEach {
            checkoutGqlDataList.add(
                Data().apply {
                    addressId = it.addressId.toLongOrZero()
                    shopOrders = mapShopProduct(it.shopProducts, prescriptionIds)
                }
            )
        }

        return checkoutGqlDataList
    }

    private fun mapShopProduct(
        shopProductCheckoutRequests: List<ShopProductCheckoutRequest>?,
        prescriptionIds: ArrayList<String>?
    ): List<ShopOrder> {
        val shopProductList = mutableListOf<ShopOrder>()
        shopProductCheckoutRequests?.forEach {
            shopProductList.add(
                ShopOrder().apply {
                    cartstring = it.cartString ?: ""
                    shopId = it.shopId
                    warehouseId = it.warehouseId
                    isPreorder = it.isPreorder
                    orderFeature = OrderFeature().apply {
                        isOrderPriority = it.isOrderPriority
                    }
                    shippingInfo = mapShippingInfo(it.shippingInfo, it.finsurance)
                    dropship = mapDropshipData(it.dropshipData, it.isDropship)
                    promos = mapPromos(it.promos)
                    bundle = mapBundle(it.productData)
                    checkoutGiftingOrderLevel = mapGiftingAddOn(it.giftingAddOnOrderLevel)
                    orderMetadata = mapOrderMetadata(it, prescriptionIds, it.promos)
                }
            )
        }

        return shopProductList
    }

    private fun mapBundle(productDataList: List<ProductDataCheckoutRequest>?): List<Bundle> {
        val bundleList = mutableListOf<Bundle>()

        val bundleIdProductsMap = mutableMapOf<String, MutableList<Product>>()
        val bundleIdGroupIdMap = mutableMapOf<String, String>()
        productDataList?.forEach {
            if (!bundleIdProductsMap.containsKey(it.bundleId)) {
                val product = mapProduct(it)
                bundleIdProductsMap[it.bundleId] = mutableListOf(product)
                bundleIdGroupIdMap[it.bundleId] = it.bundleGroupId
            } else {
                val products = bundleIdProductsMap[it.bundleId]
                val product = mapProduct(it)
                products?.add(product)
            }
        }

        bundleIdProductsMap.forEach {
            val bundle = Bundle().apply {
                bundleInfo = BundleInfo().apply {
                    if (it.key.isNotBlankOrZero()) {
                        bundleId = it.key.toLongOrZero()
                        bundleGroupId = bundleIdGroupIdMap[it.key] ?: ""
                    }
                }
                productData = it.value
            }
            bundleList.add(bundle)
        }

        return bundleList
    }

    private fun mapProduct(it: ProductDataCheckoutRequest): Product {
        val product = Product().apply {
            productId = it.productId.toString()
            isPpp = it.isPurchaseProtection
            checkoutGiftingProductLevel = mapGiftingAddOn(it.addOnGiftingProductLevelRequest)
        }
        return product
    }

    private fun mapShippingInfo(shippingInfo: ShippingInfoCheckoutRequest?, finsurance: Int): ShippingInfo {
        return ShippingInfo().apply {
            this.finsurance = finsurance
            shippingId = shippingInfo?.shippingId?.toLong() ?: 0
            spId = shippingInfo?.spId?.toLong() ?: 0
            ratesId = shippingInfo?.ratesId ?: ""
            ut = shippingInfo?.ut ?: ""
            checksum = shippingInfo?.checksum ?: ""
            val tmpRatesFeature = shippingInfo?.ratesFeature
            ratesFeature = mapRatesFeature(tmpRatesFeature)
        }
    }

    private fun mapRatesFeature(
        ratesFeature: com.tokopedia.checkout.data.model.request.common.RatesFeature?
    ): RatesFeature {
        return RatesFeature().apply {
            ontimeDeliveryGuarantee = OntimeDeliveryGuarantee().apply {
                available = ratesFeature?.ontimeDeliveryGuarantee?.available ?: false
                duration = ratesFeature?.ontimeDeliveryGuarantee?.duration ?: 0
            }
        }
    }

    private fun mapDropshipData(dropshipData: DropshipDataCheckoutRequest?, isDropship: Int): Dropship {
        return Dropship().apply {
            this.isDropship = isDropship
            name = dropshipData?.name ?: ""
            telpNo = dropshipData?.telpNo ?: ""
        }
    }

    private fun mapGiftingAddOn(listAddOnRequest: ArrayList<AddOnGiftingRequest>?): List<CheckoutGiftingAddOn> {
        val listCheckoutGiftingAddOn = arrayListOf<CheckoutGiftingAddOn>()
        listAddOnRequest?.forEach {
            val addOnRequest = CheckoutGiftingAddOn().apply {
                itemType = it.itemType
                itemId = it.itemId
                itemQty = it.itemQty
                itemMetadata = it.itemMetadata
            }
            listCheckoutGiftingAddOn.add(addOnRequest)
        }
        return listCheckoutGiftingAddOn.toList()
    }

    private fun mapOrderMetadata(
        shopProductCheckoutRequest: ShopProductCheckoutRequest,
        prescriptionIds: ArrayList<String>?,
        promos: List<PromoRequest>?
    ): List<OrderMetadata> {
        val orderMetadata = arrayListOf<OrderMetadata>()
        if (shopProductCheckoutRequest.freeShippingMetadata.isNotBlank() &&
            promos?.firstOrNull { it.type == PromoRequest.TYPE_LOGISTIC } != null
        ) {
            // only add free shipping metadata if the order contains at least 1 promo logistic
            orderMetadata.add(OrderMetadata(FREE_SHIPPING_METADATA, shopProductCheckoutRequest.freeShippingMetadata))
        }
        if (shopProductCheckoutRequest.needPrescription && prescriptionIds != null && prescriptionIds.isNotEmpty()) {
            orderMetadata.add(OrderMetadata(UPLOAD_PRESCRIPTION_META_DATA_KEY, prescriptionIds.toString()))
        }
        if (shopProductCheckoutRequest.needToSendValidationMetadata) {
            orderMetadata.add(OrderMetadata(SCHEDULE_DELIVERY_META_DATA_KEY, shopProductCheckoutRequest.validationMetadata))
        }
        return orderMetadata
    }
}
