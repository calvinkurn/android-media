package com.tokopedia.similarsearch.testinstance

import com.google.gson.Gson
import com.tokopedia.similarsearch.SimilarProductModel

private val similarProductModelEmptyResultJSONString = """
{
    "similar_products_image_search": {
      "data": {
        "products": []
      }
    }
}
""".trimIndent().replace("\n", "")

internal fun getSimilarProductModelEmptyResult() = Gson().fromJson(similarProductModelEmptyResultJSONString, SimilarProductModel::class.java)