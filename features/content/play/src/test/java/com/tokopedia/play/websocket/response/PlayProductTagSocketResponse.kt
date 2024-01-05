package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on September 15, 2021
 */
object PlayProductTagSocketResponse {

    fun generateResponseSection(
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
        for (i in 1..size) {
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
                    name: "Barang $i",
                    order: 0,
                    original_price: 123,
                    original_price_formatted: "123",
                    price: 0,
                    price_formatted: "",
                    quantity: 0,
                    shop_id: "$shopId",
                    web_link: "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447",
                    "available_buttons": [
                      {
                        "text": "+ Keranjang",
                        "color": "SECONDARY",
                        "button_type": "ATC"
                      },
                      {
                        "text": "Beli",
                        "color": "PRIMARY",
                        "button_type": "GCR"
                      }
                    ],
                    "is_pinned" : false,
                    "product_number" : 0,
                    "rating" : "",
                    "sold_quantity" : "",
                    "social_proof_rank": "Diminati",
                    "social_proof_tag_color": ["#E02954", "#FF7182"],
                    "social_proof_raw_value": 1,
                    "social_proof_type_value": "diminati"
              }
            """.trimIndent()
            if (i != size) productList += ","
        }

        var sectionList = ""
        for (x in 1..size) {
            sectionList += """{
                type: "active",
                title: "$title $x",
                countdown: {
                    copy: "Berakhir dalam"
                },
                background: {
                    gradient: [
                        "#ff23de",
                        "#2244aa"
                    ],
                    image_url: "https://via.placeholder.com/150"
                },
                start_time: "2022-01-02T15:04:05Z07:00",
                end_time: "2022-01-02T16:04:05Z07:00",
                server_time: "2022-01-02T15:14:05Z07:00",
                products : [
                    $productList
                 ]
                }
            """.trimIndent()
            if (x != size) sectionList += ","
        }
        return """
             {
                "type": "PRODUCT_TAG_UPDATE",
                "data": {
                      "sections" : [
                        $sectionList
                      ],
                      "config" : {
                        "peek_product_count" : 15,
                        "title_bottomsheet" : "Promo dan Produk Lainnya"
                      }
                }
             }
            """.trimIndent()
    }
}
