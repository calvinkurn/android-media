package com.tokopedia.discovery2.data

import com.tokopedia.kotlin.extensions.view.EMPTY

enum class ComponentSourceData(val source: String) {
    Search("search"),
    Recommendation("recom"),
    FlashSale("flash_sale"),
    SearchContent("search_content"),
    MitraRecommendation("mitra_recommendation"),
    MitraCategory("mitra_category"),
    Unknown(String.EMPTY);

    companion object {

        private val values = values()

        fun getByValue(source: String): ComponentSourceData {
            values.forEach {
                if (source == it.source) return it
            }
            return Unknown
        }
    }
}
