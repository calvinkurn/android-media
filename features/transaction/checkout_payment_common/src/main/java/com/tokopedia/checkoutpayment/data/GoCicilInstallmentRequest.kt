package com.tokopedia.checkoutpayment.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class GoCicilAddressRequest(
    val addressStreet: String = "",
    val provinceName: String = "",
    val cityName: String = "",
    val country: String = "",
    val postalCode: String = ""
)

data class GoCicilProductRequest(
    var productId: String = "",
    var productPrice: Double = 0.0,
    var orderQuantity: Int = 0,
    var categoryId: Long = 0,
    var lastLevelCategory: String = "",
    var categoryIdentifier: String = ""
)

data class GoCicilInstallmentRequest(
    val gatewayCode: String = "",
    val merchantCode: String = "",
    val profileCode: String = "",
    val userId: Long = 0,
    val paymentAmount: Double = 0.0,
    val merchantType: String = "",
    val address: GoCicilAddressRequest = GoCicilAddressRequest(),
    val shopId: String = "",
    val products: List<GoCicilProductRequest> = emptyList(),
    val promoCodes: List<String> = emptyList(),
    val additionalData: String = "",
    val detailData: PaymentRequest
) {
    val userDefinedValue
        get() = JsonObject().apply {
            addProperty(USER_DEFINED_VALUE_KEY_USER_ID, userId)
        }.toString()

    private val destinationAddress
        get() = JsonObject().apply {
            addProperty(ORDER_METADATA_DEST_ADDRESS_ADDRESS, address.addressStreet)
            addProperty(ORDER_METADATA_DEST_ADDRESS_STATE, address.provinceName)
            addProperty(ORDER_METADATA_DEST_ADDRESS_CITY, address.cityName)
            addProperty(ORDER_METADATA_DEST_ADDRESS_COUNTRY, address.country)
            addProperty(ORDER_METADATA_DEST_ADDRESS_POSTAL_CODE, address.postalCode)
        }

    private val sellerData
        get() = JsonObject().apply {
            addProperty(ORDER_METADATA_SELLER_DATA_ID, shopId)
        }

    private val productData
        get() = JsonArray().apply {
            products.forEach { product ->
                add(
                    JsonObject().apply {
                        addProperty(ORDER_METADATA_PRODUCT_DATA_ID, product.productId)
                        addProperty(ORDER_METADATA_PRODUCT_DATA_PRICE, product.productPrice)
                        addProperty(ORDER_METADATA_PRODUCT_DATA_QUANTITY, product.orderQuantity)
                        add(
                            ORDER_METADATA_PRODUCT_DATA_CATEGORY,
                            JsonObject().apply {
                                addProperty(ORDER_METADATA_PRODUCT_CATEGORY_ID, product.categoryId)
                                addProperty(ORDER_METADATA_PRODUCT_CATEGORY_NAME, product.lastLevelCategory)
                                addProperty(ORDER_METADATA_PRODUCT_CATEGORY_IDENTIFIER, product.categoryIdentifier)
                            }
                        )
                    }
                )
            }
        }

    val orderMetadata
        get() = JsonObject().apply {
            add(
                ORDER_METADATA_KEY_ORDER_DATA,
                JsonArray().apply {
                    add(
                        JsonObject().apply {
                            addProperty(ORDER_METADATA_KEY_MERCHANT_TYPE, merchantType)
                            addProperty(ORDER_METADATA_KEY_ORDER_AMOUNT, paymentAmount)
                            add(ORDER_METADATA_KEY_DEST_ADDRESS, destinationAddress)
                            add(ORDER_METADATA_KEY_SELLER_DATA, sellerData)
                            add(ORDER_METADATA_KEY_PRODUCT_DATA, productData)
                        }
                    )
                }
            )
        }.toString()

    val promoParam
        get() = promoCodes.joinToString(",")

    companion object {
        private const val USER_DEFINED_VALUE_KEY_USER_ID = "user_id"

        private const val ORDER_METADATA_KEY_ORDER_DATA = "order_data"
        private const val ORDER_METADATA_KEY_MERCHANT_TYPE = "merchant_type"
        private const val ORDER_METADATA_KEY_ORDER_AMOUNT = "order_amount"
        private const val ORDER_METADATA_KEY_DEST_ADDRESS = "dest_address"
        private const val ORDER_METADATA_KEY_SELLER_DATA = "seller_data"
        private const val ORDER_METADATA_KEY_PRODUCT_DATA = "product_data"

        private const val ORDER_METADATA_DEST_ADDRESS_ADDRESS = "address"
        private const val ORDER_METADATA_DEST_ADDRESS_STATE = "state"
        private const val ORDER_METADATA_DEST_ADDRESS_CITY = "city"
        private const val ORDER_METADATA_DEST_ADDRESS_COUNTRY = "country"
        private const val ORDER_METADATA_DEST_ADDRESS_POSTAL_CODE = "postal_code"

        private const val ORDER_METADATA_SELLER_DATA_ID = "shop_id"

        private const val ORDER_METADATA_PRODUCT_DATA_ID = "product_id"
        private const val ORDER_METADATA_PRODUCT_DATA_PRICE = "price"
        private const val ORDER_METADATA_PRODUCT_DATA_QUANTITY = "quantity"
        private const val ORDER_METADATA_PRODUCT_DATA_CATEGORY = "product_category"

        private const val ORDER_METADATA_PRODUCT_CATEGORY_ID = "id"
        private const val ORDER_METADATA_PRODUCT_CATEGORY_NAME = "name"
        private const val ORDER_METADATA_PRODUCT_CATEGORY_IDENTIFIER = "identifier"
    }
}
