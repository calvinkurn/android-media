package com.tokopedia.oneclickcheckout.order.data.gocicil

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress

class GoCicilInstallmentRequest(
        val gatewayCode: String = "",
        val merchantCode: String = "",
        val profileCode: String = "",
        val userId: String = "",
        val paymentAmount: Double = 0.0,
        val merchantType: String = "",
        val address: OrderProfileAddress = OrderProfileAddress(),
        val products: MutableList<OrderProduct> = ArrayList()
) {
    val userDefinedValue
        get() = JsonObject().apply {
            addProperty(USER_DEFINED_VALUE_KEY_USER_ID, userId.toLongOrZero())
        }.toString()

    private val destinationAddress
        get() = JsonObject().apply {
            addProperty(ORDER_METADATA_DEST_ADDRESS_ADDRESS, address.addressStreet)
            addProperty(ORDER_METADATA_DEST_ADDRESS_STATE, address.addressStreet)
            addProperty(ORDER_METADATA_DEST_ADDRESS_CITY, address.cityName)
            addProperty(ORDER_METADATA_DEST_ADDRESS_COUNTRY, address.country)
            addProperty(ORDER_METADATA_DEST_ADDRESS_POSTAL_CODE, address.postalCode)
        }

    private val productData
        get() = JsonArray().apply {
            products.forEach { product ->
                add(JsonObject().apply {
                    addProperty(ORDER_METADATA_PRODUCT_DATA_ID, product.productId)
                    addProperty(ORDER_METADATA_PRODUCT_DATA_PRICE, product.productPrice)
                    addProperty(ORDER_METADATA_PRODUCT_DATA_QUANTITY, product.orderQuantity)
                    add(ORDER_METADATA_PRODUCT_DATA_CATEGORY, JsonObject().apply {
                        addProperty(ORDER_METADATA_PRODUCT_CATEGORY_ID, product.categoryId)
                        addProperty(ORDER_METADATA_PRODUCT_CATEGORY_NAME, product.lastLevelCategory)
                        addProperty(ORDER_METADATA_PRODUCT_CATEGORY_IDENTIFIER, product.categoryIdentifier)
                    })
                })
            }
        }

    val orderMetadata
        get() = JsonObject().apply {
            add(ORDER_METADATA_KEY_ORDER_DATA, JsonArray().apply {
                add(JsonObject().apply {
                    addProperty(ORDER_METADATA_KEY_MERCHANT_TYPE, merchantType)
                    addProperty(ORDER_METADATA_KEY_ORDER_AMOUNT, paymentAmount)
                    add(ORDER_METADATA_KEY_DEST_ADDRESS, destinationAddress)
                    add(ORDER_METADATA_KEY_PRODUCT_DATA, productData)
                })
            })
        }.toString()

    companion object {
        private const val USER_DEFINED_VALUE_KEY_USER_ID = "user_id"

        private const val ORDER_METADATA_KEY_ORDER_DATA = "order_data"
        private const val ORDER_METADATA_KEY_MERCHANT_TYPE = "merchant_type"
        private const val ORDER_METADATA_KEY_ORDER_AMOUNT = "order_amount"
        private const val ORDER_METADATA_KEY_DEST_ADDRESS = "dest_address"
        private const val ORDER_METADATA_KEY_PRODUCT_DATA = "product_data"

        private const val ORDER_METADATA_DEST_ADDRESS_ADDRESS = "address"
        private const val ORDER_METADATA_DEST_ADDRESS_STATE = "state"
        private const val ORDER_METADATA_DEST_ADDRESS_CITY = "city"
        private const val ORDER_METADATA_DEST_ADDRESS_COUNTRY = "country"
        private const val ORDER_METADATA_DEST_ADDRESS_POSTAL_CODE = "postal_code"

        private const val ORDER_METADATA_PRODUCT_DATA_ID = "product_id"
        private const val ORDER_METADATA_PRODUCT_DATA_PRICE = "price"
        private const val ORDER_METADATA_PRODUCT_DATA_QUANTITY = "quantity"
        private const val ORDER_METADATA_PRODUCT_DATA_CATEGORY = "product_category"

        private const val ORDER_METADATA_PRODUCT_CATEGORY_ID = "id"
        private const val ORDER_METADATA_PRODUCT_CATEGORY_NAME = "name"
        private const val ORDER_METADATA_PRODUCT_CATEGORY_IDENTIFIER = "identifier"
    }
}
