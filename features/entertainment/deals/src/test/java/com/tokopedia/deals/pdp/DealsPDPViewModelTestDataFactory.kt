package com.tokopedia.deals.pdp

import com.google.gson.Gson
import com.tokopedia.common_entertainment.data.DealsVerifyResponse
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.pdp.data.DealsProductDetail
import com.tokopedia.deals.pdp.data.DealsProductEventContent
import com.tokopedia.deals.pdp.data.DealsRatingResponse
import com.tokopedia.deals.pdp.data.DealsRatingUpdateResponse
import com.tokopedia.deals.pdp.data.DealsTrackingResponse

fun createPDPData(): DealsProductDetail {
    return Gson().fromJson(
        DealsJsonMapper.getJson("pdp.json"),
        DealsProductDetail::class.java
    )
}

fun createPDPEmptyMediaData(): DealsProductDetail {
    return Gson().fromJson(
        DealsJsonMapper.getJson("pdp_media_empty.json"),
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

fun createRating(): DealsRatingResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("rating.json"),
        DealsRatingResponse::class.java
    )
}

fun createRatingUpdate(): DealsRatingUpdateResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("rating_update.json"),
        DealsRatingUpdateResponse::class.java
    )
}

fun createTracking(): DealsTrackingResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("tracking.json"),
        DealsTrackingResponse::class.java
    )
}

fun createVerify(): DealsVerifyResponse {
    return Gson().fromJson(
        DealsJsonMapper.getJson("verify.json"),
        DealsVerifyResponse::class.java
    )
}

