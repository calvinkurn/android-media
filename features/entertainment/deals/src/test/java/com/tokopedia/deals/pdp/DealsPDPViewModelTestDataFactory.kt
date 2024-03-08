package com.tokopedia.deals.pdp

import com.google.gson.Gson
import com.tokopedia.common_entertainment.data.DealsVerifyResponse
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.common.model.response.SearchData

fun createPDPData(): com.tokopedia.deals.ui.pdp.data.DealsProductDetail {
    return Gson().fromJson(
        DealsJsonMapper.getJson("pdp.json"),
        com.tokopedia.deals.ui.pdp.data.DealsProductDetail::class.java
    )
}

fun createPDPEmptyMediaData(): com.tokopedia.deals.ui.pdp.data.DealsProductDetail {
    return Gson().fromJson(
        DealsJsonMapper.getJson("pdp_media_empty.json"),
        com.tokopedia.deals.ui.pdp.data.DealsProductDetail::class.java
    )
}

fun createContentById(): com.tokopedia.deals.ui.pdp.data.DealsProductEventContent {
    return Gson().fromJson(
        DealsJsonMapper.getJson("content_by_id.json"),
        com.tokopedia.deals.ui.pdp.data.DealsProductEventContent::class.java
    )
}

fun createContentByIdEmpty(): com.tokopedia.deals.ui.pdp.data.DealsProductEventContent {
    return Gson().fromJson(
        DealsJsonMapper.getJson("content_by_id_empty.json"),
        com.tokopedia.deals.ui.pdp.data.DealsProductEventContent::class.java
    )
}

fun createRecommendation(): SearchData {
    return Gson().fromJson(
        DealsJsonMapper.getJson("recommendation.json"),
        SearchData::class.java
    )
}

fun createRating(): com.tokopedia.deals.ui.pdp.data.DealsRatingResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("rating.json"),
        com.tokopedia.deals.ui.pdp.data.DealsRatingResponse::class.java
    )
}

fun createRatingUpdate(): com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("rating_update.json"),
        com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse::class.java
    )
}

fun createTracking(): com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("tracking.json"),
        com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse::class.java
    )
}

fun createVerify(): DealsVerifyResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("verify.json"),
        DealsVerifyResponse::class.java
    )
}

fun createVerifyError(): DealsVerifyResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("verify_error.json"),
        DealsVerifyResponse::class.java
    )
}

