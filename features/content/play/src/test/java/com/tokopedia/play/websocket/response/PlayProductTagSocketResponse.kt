package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on September 15, 2021
 */
object PlayProductTagSocketResponse {

    fun generateResponse(
        size: Int = 2,
        shopId: String = "123",
        imageUrl: String = "https://www.tokopedia.com",
        title: String = "Barang Murah",
        isVariantAvailable: Boolean = false,
        minQty: Int = 1,
        isFreeShipping: Boolean = false,
        applink: String? = null
    ): String {
        var productList = ""
        for(i in 1..size) {
            productList += """
                {
                    app_link: "$applink",
                    discount: 0,
                    id: $i,
                    image_url: "$imageUrl",
                    is_available: false,
                    is_free_shipping: $isFreeShipping,
                    is_variant: $isVariantAvailable,
                    min_quantity: $minQty,
                    name: "$title $i",
                    order: 0,
                    original_price: 123,
                    original_price_formatted: "123",
                    price: 0,
                    price_formatted: "",
                    quantity: 0,
                    shop_id: "$shopId",
                    web_link: "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447"
              }
            """.trimIndent()

            if(i != size) productList += ","
        }

        return """
            {
                "type": "PRODUCT_TAG",
                "data": {
                      "is_show_product_tagging" : true,
                      "products" : [
                            $productList      
                      ]
                }
            }
        """.trimIndent()
    }

}