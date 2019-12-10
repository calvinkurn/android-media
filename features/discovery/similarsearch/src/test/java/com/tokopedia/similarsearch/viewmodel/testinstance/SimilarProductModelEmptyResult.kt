package com.tokopedia.similarsearch.viewmodel.testinstance

import com.google.gson.Gson
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel

private val similarProductModelEmptyResultJSONString = """
{
    "similar_products_image_search": {
        "data": {
            "products":[],
            "originalProduct": {
                "id":0,
                "name":"",
                "url":"",
                "image_url":"",
                "image_url_700":"",
                "price":"",
                "shop": {
                    "id":0,
                    "is_gold_shop":false,
                    "location":"",
                    "city":"",
                    "reputation":"",
                    "clover":"",
                    "is_official":false
                },
                "badges":[],
                "category_id":0,
                "category_name":"",
                "rating":0,
                "count_review":0,
                "original_price":"",
                "discount_expired_time":"",
                "discount_start_time":"",
                "discount_percentage":0,
                "wishlist":false
            }
        }
    }
}
""".trimIndent().replace("\n", "")

internal fun getSimilarProductModelEmptyResult() = Gson().fromJson(similarProductModelEmptyResultJSONString, SimilarProductModel::class.java)