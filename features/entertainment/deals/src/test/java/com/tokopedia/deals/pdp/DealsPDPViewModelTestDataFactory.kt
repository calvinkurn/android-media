package com.tokopedia.deals.pdp

import com.google.gson.Gson
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.pdp.data.DealsProductDetail
import com.tokopedia.deals.pdp.data.DealsProductEventContent


fun createPDPData(): DealsProductDetail {
    return Gson().fromJson(
        DealsJsonMapper.getJson("pdp.json"),
        DealsProductDetail::class.java
    )
}

fun createContentById(): DealsProductEventContent {
    return Gson().fromJson(
        DealsJsonMapper.getJson("content_by_id.json"),
        DealsProductEventContent::class.java
    )
}

fun createContentByIdEmpty(): DealsProductEventContent {
    return Gson().fromJson(
        DealsJsonMapper.getJson("content_by_id_empty.json"),
        DealsProductEventContent::class.java
    )
}

fun createRecommendation(): SearchData {
    return Gson().fromJson(
        DealsJsonMapper.getJson("recommendation.json"),
        SearchData::class.java
    )
}

