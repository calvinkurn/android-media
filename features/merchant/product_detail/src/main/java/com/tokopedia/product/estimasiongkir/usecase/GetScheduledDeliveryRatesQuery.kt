package com.tokopedia.product.estimasiongkir.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetScheduledDeliveryRatesQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "ScheduledDeliveryRates"
    private const val PARAM_ORIGIN = "origin"
    private const val PARAM_DESTINATION = "destination"
    private const val PARAM_WAREHOUSE_ID = "warehouse_id"
    private const val PARAM_WEIGHT = "weight"
    private const val PARAM_SHOP_ID = "shop_id"
    private const val PARAM_UNIQUE_ID = "unique_id"
    private const val PARAM_PRODUCTS = "products"
    private const val PARAM_BO_METADATA = "bo_metadata"
    private const val PARAM_SOURCE = "source"

    private const val SOURCE = "pdp"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            query ScheduledDeliveryRates($$PARAM_ORIGIN: String!, $$PARAM_DESTINATION: String!, $$PARAM_WAREHOUSE_ID: Int64!, $$PARAM_WEIGHT: String!, $$PARAM_SHOP_ID: Int64, $$PARAM_UNIQUE_ID: String, $$PARAM_PRODUCTS: String, $$PARAM_BO_METADATA: String, $$PARAM_SOURCE: String) {
                ongkirGetScheduledDeliveryRates(input: {$PARAM_ORIGIN: $$PARAM_ORIGIN, $PARAM_DESTINATION: $$PARAM_DESTINATION, $PARAM_WAREHOUSE_ID: $$PARAM_WAREHOUSE_ID, $PARAM_WEIGHT: $$PARAM_WEIGHT, $PARAM_SHOP_ID: $$PARAM_SHOP_ID, $PARAM_UNIQUE_ID: $$PARAM_UNIQUE_ID, $PARAM_PRODUCTS: $$PARAM_PRODUCTS, $PARAM_BO_METADATA: $$PARAM_BO_METADATA, $PARAM_SOURCE: $$PARAM_SOURCE}) {
                    data {
                        delivery_services {
                            title
                            title_label
                            available
                            hidden
                            delivery_products {
                                title
                                available
                                hidden
                                text
                                recommend
                                text_real_price
                                text_final_price
                            }
                        }
                    }
                }
            }""".trimIndent()

    override fun getTopOperationName() = OPERATION_NAME

    fun createParams(
        origin: String,
        destination: String,
        warehouseId: Long,
        weight: String,
        shopId: Long,
        uniqueId: String,
        products: String = "",
        boMetadata: String = ""
    ) = mapOf(
        "destination" to "2274||,",
        "weight" to "2.0",
        "origin" to "579|20117|3.6095088000000146,98.66532999999998",
        "shop_id" to 6552935,
        "unique_id" to "-6552935-0-343034",
        "products" to "[{\"is_free_shipping\":true,\"is_free_shipping_tc\":false,\"product_id\":2148326513}]",
        "bo_metadata" to "{\"bo_metadata\":{\"bo_type\":1,\"bo_eligibilities\":[{\"key\":\"campaign_ids\",\"value\":\"3,8,9,59\"}]}}",
        "warehouse_id" to 12212,
        "source" to "pdp"
//        PARAM_ORIGIN to origin,
//        PARAM_DESTINATION to destination,
//        PARAM_WAREHOUSE_ID to warehouseId,
//        PARAM_WEIGHT to weight,
//        PARAM_SHOP_ID to shopId,
//        PARAM_UNIQUE_ID to uniqueId,
//        PARAM_PRODUCTS to products,
//        PARAM_BO_METADATA to boMetadata,
//        PARAM_SOURCE to SOURCE
    )
}
